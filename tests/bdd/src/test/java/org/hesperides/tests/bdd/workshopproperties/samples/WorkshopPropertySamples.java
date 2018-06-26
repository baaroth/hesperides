/*
 *
 * This file is part of the Hesperides distribution.
 * (https://github.com/voyages-sncf-technologies/hesperides)
 * Copyright (c) 2016 VSCT.
 *
 * Hesperides is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, version 3.
 *
 * Hesperides is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */
package org.hesperides.tests.bdd.workshopproperties.samples;

import org.hesperides.presentation.io.WorkshopPropertyInput;
import org.hesperides.presentation.io.WorkshopPropertyOutput;
import org.hesperides.presentation.io.WorkshopPropertyUpdateInput;

public class WorkshopPropertySamples {
    public static final String DEFAULT_KEY = "key";
    public static final String DEFAULT_VALUE = "value";

    public static WorkshopPropertyInput getWorkshopPropertyInputWithDefaultValues() {
        return getWorkshopPropertyInputWithValue(DEFAULT_VALUE);
    }

    public static WorkshopPropertyOutput getWorkshopPropertyOutputWithDefaultValues() {
        return getWorkshopPropertyOutputWithValue(DEFAULT_VALUE, 1);
    }

    public static WorkshopPropertyInput getWorkshopPropertyInputWithValue(String value) {
        return new WorkshopPropertyInput(DEFAULT_KEY, value);
    }

    public static WorkshopPropertyUpdateInput buildWorkshopPropertyUpdateInput(String value, int version) {
        return new WorkshopPropertyUpdateInput(value, version);
    }

    public static WorkshopPropertyOutput getWorkshopPropertyOutputWithValue(String value, int version) {
        return new WorkshopPropertyOutput(DEFAULT_KEY, value, DEFAULT_KEY + ":" + value, version);
    }
}
