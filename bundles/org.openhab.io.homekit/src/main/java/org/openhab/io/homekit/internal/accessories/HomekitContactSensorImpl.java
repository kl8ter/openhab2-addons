/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.homekit.internal.accessories;

import static org.openhab.io.homekit.internal.HomekitCharacteristicType.CONTACT_SENSOR_STATE;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.openhab.io.homekit.internal.HomekitAccessoryUpdater;
import org.openhab.io.homekit.internal.HomekitSettings;
import org.openhab.io.homekit.internal.HomekitTaggedItem;

import io.github.hapjava.accessories.ContactSensorAccessory;
import io.github.hapjava.characteristics.HomekitCharacteristicChangeCallback;
import io.github.hapjava.characteristics.impl.contactsensor.ContactStateEnum;
import io.github.hapjava.services.impl.ContactSensorService;

/**
 *
 * @author Philipp Arndt - Initial contribution
 */
public class HomekitContactSensorImpl extends AbstractHomekitAccessoryImpl implements ContactSensorAccessory {
    private final BooleanItemReader contactSensedReader;

    public HomekitContactSensorImpl(HomekitTaggedItem taggedItem, List<HomekitTaggedItem> mandatoryCharacteristics,
            HomekitAccessoryUpdater updater, HomekitSettings settings) throws IncompleteAccessoryException {
        super(taggedItem, mandatoryCharacteristics, updater, settings);
        contactSensedReader = createBooleanReader(CONTACT_SENSOR_STATE, OnOffType.ON, OpenClosedType.OPEN);
        getServices().add(new ContactSensorService(this));
    }

    @Override
    public CompletableFuture<ContactStateEnum> getCurrentState() {
        return CompletableFuture.completedFuture(
                contactSensedReader.getValue() ? ContactStateEnum.NOT_DETECTED : ContactStateEnum.DETECTED);
    }

    @Override
    public void subscribeContactState(HomekitCharacteristicChangeCallback callback) {
        subscribe(CONTACT_SENSOR_STATE, callback);
    }

    @Override
    public void unsubscribeContactState() {
        unsubscribe(CONTACT_SENSOR_STATE);
    }
}