/*
 * Copyright 2017 Hammock and its contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ws.ament.hammock.jwt.bean;

import org.eclipse.microprofile.jwt.Claim;

import java.lang.reflect.Type;

public class ClaimDefinition {
    final Claim claim;
    final Type returnType;
    final Type typeParameter;

    ClaimDefinition(Claim claim, Type returnType, Type typeParameter) {
        this.claim = claim;
        this.returnType = returnType;
        this.typeParameter = typeParameter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClaimDefinition that = (ClaimDefinition) o;

        if (claim != null ? !claim.equals(that.claim) : that.claim != null) return false;
        if (returnType != null ? !returnType.equals(that.returnType) : that.returnType != null) return false;
        return typeParameter != null ? typeParameter.equals(that.typeParameter) : that.typeParameter == null;
    }

    @Override
    public int hashCode() {
        int result = claim != null ? claim.hashCode() : 0;
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        result = 31 * result + (typeParameter != null ? typeParameter.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClaimDefinition{" + "claim=" + claim +
                ", returnType=" + returnType +
                ", typeParameter=" + typeParameter +
                '}';
    }
}
