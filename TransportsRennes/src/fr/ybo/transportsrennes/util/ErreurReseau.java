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
 * 
 * Contributors:
 *     ybonnel - initial API and implementation
 */
package fr.ybo.transportsrennes.util;

public class ErreurReseau extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErreurReseau(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ErreurReseau() {
		super("Erreur réseau détectée");
	}

	public ErreurReseau(Throwable throwable) {
		super(throwable);
	}

}