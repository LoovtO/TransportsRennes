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

package fr.ybo.gtfs.modele;

import fr.ybo.gtfs.csv.annotation.BaliseCsv;
import fr.ybo.gtfs.csv.annotation.FichierCsv;
import fr.ybo.gtfs.csv.moteur.adapter.AdapterInteger;

@FichierCsv("lignes.txt")
public class Ligne {
	@BaliseCsv("id")
	public String id;
	@BaliseCsv("nom_court")
	public String nomCourt;
	@BaliseCsv("nom_long")
	public String nomLong;
	@BaliseCsv(value = "ordre", adapter = AdapterInteger.class)
	public Integer ordre;
}