/*
 * Copyright © 2015 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.ikm.tinkar.entity;

import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.util.time.DateTimeUtil;
import dev.ikm.tinkar.component.Component;
import dev.ikm.tinkar.component.Stamp;
import dev.ikm.tinkar.component.Version;
import dev.ikm.tinkar.terms.ConceptFacade;
import dev.ikm.tinkar.terms.State;
import org.eclipse.collections.api.list.ImmutableList;

import static dev.ikm.tinkar.common.util.time.DateTimeUtil.SEC_FORMATTER;

public interface StampEntity<V extends StampEntityVersion> extends Entity<V>,
        Stamp<V>, Component, Version, IdentifierData {
    @Override
    default State state() {
        return lastVersion().state();
    }

    @Override
    default long time() {
        return lastVersion().time();
    }

    @Override
    default ConceptFacade author() {
        return Entity.provider().getEntityFast(authorNid());
    }

    @Override
    default ConceptFacade module() {
        return Entity.provider().getEntityFast(moduleNid());
    }

    @Override
    default ConceptFacade path() {
        return Entity.provider().getEntityFast(pathNid());
    }

    @Override
    StampEntity stamp();

    default int pathNid() {
        return lastVersion().pathNid();
    }

    default int moduleNid() {
        return lastVersion().moduleNid();
    }

    default int authorNid() {
        return lastVersion().authorNid();
    }

    default StampEntityVersion lastVersion() {
        if (versions().size() == 1) {
            return versions().get(0);
        }
        StampEntityVersion latest = null;
        for (StampEntityVersion version : versions()) {
            if (version.time() == Long.MIN_VALUE) {
                // if canceled (Long.MIN_VALUE), latest is canceled.
                return version;
            } else if (latest == null) {
                latest = version;
            } else if (latest.time() == Long.MAX_VALUE) {
                latest = version;
            } else if (version.time() == Long.MAX_VALUE) {
                // ignore uncommitted version;
            } else if (latest.time() < version.time()) {
                latest = version;
            }
        }
        return latest;
    }

    @Override
    ImmutableList<V> versions();

    @Override
    default boolean canceled() {
        return Entity.super.canceled();
    }

    default int stateNid() {
        return lastVersion().stateNid();
    }

    default String describe() {
        return "s:" + PrimitiveData.text(stateNid()) +
                " t:" + DateTimeUtil.format(time(), SEC_FORMATTER) +
                " a:" + PrimitiveData.text(authorNid()) +
                " m:" + PrimitiveData.text(moduleNid()) +
                " p:" + PrimitiveData.text(pathNid());
    }
}
