/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.pg;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.jdbi.v3.StatementContext;
import org.jdbi.v3.argument.Argument;

public class ArrayArgument implements Argument {

    private final String elementType;
    private final Object array;

    private ArrayArgument(String elementType, Object array) {
        this.elementType = elementType;
        this.array = array;
    }

    public static ArrayArgument fromArray(String elementType, Object[] array) {
        return fromAnyArray(elementType, array);
    }

    public static ArrayArgument fromAnyArray(String elementType, Object array) {
        return new ArrayArgument(elementType, array);
    }

    public static ArrayArgument fromVarargs(String elementType, Object... elements) {
        return fromArray(elementType, elements);
    }

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
        Array sqlArray = statement.getConnection().createArrayOf(elementType, (Object[]) array);
        statement.setArray(position, sqlArray);
    }
}
