/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package network.misq.common.timer;

import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

public class MasterTimer {
    private static final java.util.Timer timer = new java.util.Timer();
    // A frame rate of 60 fps is about 16 ms
    public static final long FRAME_INTERVAL_MS = 16;

    static {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                UserThread.execute(() -> listeners.forEach(Runnable::run));
            }
        }, FRAME_INTERVAL_MS, FRAME_INTERVAL_MS);
    }

    private static final Set<Runnable> listeners = new CopyOnWriteArraySet<>();

    public static void addListener(Runnable runnable) {
        listeners.add(runnable);
    }

    public static void removeListener(Runnable runnable) {
        listeners.remove(runnable);
    }
}
