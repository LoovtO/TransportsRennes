/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.code.geocoder.model;

import java.util.List;

/**
 * @author <a href="mailto:panchmp@gmail.com">Michael Panchenko</a>
 */
public class GeocoderAddressComponent {
	protected String longName;
	protected String shortName;
	protected List<String> types;

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		GeocoderAddressComponent that = (GeocoderAddressComponent) o;

		return !(longName != null ? !longName.equals(that.longName) : that.longName != null) &&
				!(shortName != null ? !shortName.equals(that.shortName) : that.shortName != null) &&
				!(types != null ? !types.equals(that.types) : that.types != null);

	}

	@Override
	public int hashCode() {
		int result = longName != null ? longName.hashCode() : 0;
		result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
		result = 31 * result + (types != null ? types.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("GeocoderAddressComponent");
		sb.append("{longName='").append(longName).append('\'');
		sb.append(", shortName='").append(shortName).append('\'');
		sb.append(", types=").append(types);
		sb.append('}');
		return sb.toString();
	}
}