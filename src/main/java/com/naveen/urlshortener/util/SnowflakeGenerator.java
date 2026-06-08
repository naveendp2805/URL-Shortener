package com.naveen.urlshortener.util;

public class SnowflakeGenerator {

    private static final long CUSTOM_EPOCH = 1735689600000L;

    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT = MACHINE_ID_BITS + SEQUENCE_BITS;

    private final long machineId;

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    public SnowflakeGenerator(long machineId) {

        if(machineId < 0 || machineId > MAX_MACHINE_ID)
            throw new IllegalArgumentException("Invalid machine id");

        this.machineId = machineId;
    }

    public synchronized long generateId(){

        long currentTimestamp = getCurrentTimestamp();

        if(currentTimestamp < lastTimestamp)
            throw new RuntimeException("Clock moved backwards");

        if(currentTimestamp == lastTimestamp){

            sequence = (sequence + 1) & MAX_SEQUENCE;

            if(sequence == 0)
                currentTimestamp = waitForNextMillis(currentTimestamp);

        }else{
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - CUSTOM_EPOCH) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;
    }

    private long waitForNextMillis(long currentTimestamp){

        while(currentTimestamp == lastTimestamp)
            currentTimestamp = getCurrentTimestamp();

        return currentTimestamp;
    }

    private long getCurrentTimestamp(){
        return System.currentTimeMillis();
    }
}