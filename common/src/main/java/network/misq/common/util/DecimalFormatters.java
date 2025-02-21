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

package network.misq.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import network.misq.common.data.Couple;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalFormatters {
    /**
     * Wrapper to make DecimalFormat immutable and expose only what we use.
     */
    public record Format(java.text.DecimalFormat decimalFormat) {

        public final String format(long number) {
            return decimalFormat.format(number);
        }

        public final String format(double number) {
            return decimalFormat.format(number);
        }
    }

    /**
     * Caches formatters which have the same parameters. We use formatters as stateless immutable objects.
     */
    private static final LoadingCache<Couple<Locale, Integer>, Format> decimalFormatCache = CacheBuilder.newBuilder()
            .build(CacheLoader.from(DecimalFormatters::getDecimalFormat));

    /**
     * @param locale    The locale to be used
     * @param precision The precision
     * @return Returns cached DecimalFormat object.
     */
    public static Format getDecimalFormat(Locale locale, int precision) {
        return decimalFormatCache.getUnchecked(new Couple<>(locale, precision));
    }

    private static Format getDecimalFormat(Couple<Locale, Integer> couple) {
        Locale locale = couple.first();
        int precision = couple.second();
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        if (precision > 0) {
            decimalFormat.applyPattern(getPattern(precision));
        } else {
            decimalFormat.applyPattern("0");
        }

        return new Format(decimalFormat);
    }

    private static String getPattern(int precision) {
        return "0." + "0".repeat(Math.max(0, precision));
    }
}