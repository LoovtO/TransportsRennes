/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.ybo.transportsrenneshelper.generateurmodele.modele;

import fr.ybo.transportsrenneshelper.annotation.BaliseCsv;
import fr.ybo.transportsrenneshelper.annotation.FichierCsv;
import fr.ybo.transportsrenneshelper.moteurcsv.adapter.AdapterInteger;

@FichierCsv("trajets.txt")
public class Trajet {
	@BaliseCsv(value = "id", adapter = AdapterInteger.class)
	public int id;
	@BaliseCsv(value = "calendrier_id", adapter = AdapterInteger.class)
	public int calendrierId;
	@BaliseCsv("ligne_id")
	public String ligneId;
	@BaliseCsv(value = "direction_id", adapter = AdapterInteger.class)
	public int directionId;
}