/*
 * Copyright 2018 Netflix, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package com.netflix.zuul.stats;

import com.netflix.spectator.api.Registry;
import com.netflix.spectator.api.Spectator;
import com.netflix.spectator.api.patterns.PolledMeter;
import com.netflix.zuul.stats.monitoring.MonitorRegistry;
import com.netflix.zuul.stats.monitoring.NamedCount;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Simple Epic counter with a name and a count.
 *
 * @author mhawthorne
 */
public class NamedCountingMonitor implements NamedCount {

    private final String name;

    private final AtomicLong count = new AtomicLong();

    public NamedCountingMonitor(String name) {
        this.name = name;
        Registry registry = Spectator.globalRegistry();
        PolledMeter.using(registry)
                .withId(registry.createId("zuul.ErrorStatsData", "ID", name))
                .monitorValue(this, NamedCountingMonitor::getCount);
    }

    /**
     * registers this objects
     */
    public NamedCountingMonitor register() {
        MonitorRegistry.getInstance().registerObject(this);
        return this;
    }

    /**
     * increments the counter
     */
    public long increment() {
        return this.count.incrementAndGet();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the current count
     */
    @Override
    public long getCount() {
        return this.count.get();
    }
}
