package com.aweqy.jewelrypurchaseweb.Util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 雪花算法（订单号生成）
 */
public class OrderIdGenerator {
    // 日期格式（作为订单号前缀）
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    // 机器ID位数（最多支持32台机器）
    private static final long WORKER_ID_BITS = 5L;

    // 序列号位数（每毫秒最多生成31个ID）
    private static final long SEQUENCE_BITS = 5L;

    // 最大机器ID（根据位数计算）
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    // 机器ID左移位数
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 时间戳左移位数
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 序列号掩码（防止溢出）
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // 实例参数
    private final long workerId;    // 机器ID（0-31）
    private final AtomicLong sequence = new AtomicLong(0);
    private volatile String lastDate;
    private volatile long lastTimestamp = -1L;

    public OrderIdGenerator(long workerId) {
        if (workerId < 0 || workerId > MAX_WORKER_ID) {
            throw new IllegalArgumentException("Worker ID必须在0到" + MAX_WORKER_ID + "之间");
        }
        this.workerId = workerId;
        this.lastDate = LocalDate.now().format(DATE_FORMATTER);
    }

    public synchronized String nextId() {
        // 获取当前时间戳（秒级精度，减少位数）
        long timestamp = System.currentTimeMillis() / 1000;

        // 检查日期变化（每天重置序列号）
        String currentDate = LocalDate.now().format(DATE_FORMATTER);
        if (!currentDate.equals(lastDate)) {
            sequence.set(0);
            lastDate = currentDate;
        }

        // 处理时钟回拨
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("系统时钟回退，拒绝生成ID");
        }

        // 同一秒内生成
        if (timestamp == lastTimestamp) {
            long currentSequence = sequence.incrementAndGet() & SEQUENCE_MASK;
            if (currentSequence == 0) {
                // 当前秒序列号用尽，等待下一秒
                timestamp = tilNextSecond(lastTimestamp);
            }
        } else {
            sequence.set(0);
        }

        lastTimestamp = timestamp;

        // 组合订单号：日期(8) + 时间戳(5) + 机器ID(3) + 序列号(3)
        return String.format("%s%05d%03d%03d",
                currentDate,
                timestamp % 100000,  // 取后5位时间戳
                workerId,
                sequence.getAndIncrement() & SEQUENCE_MASK
        );
    }

    private long tilNextSecond(long lastTimestamp) {
        long timestamp = System.currentTimeMillis() / 1000;
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis() / 1000;
        }
        return timestamp;
    }

}
