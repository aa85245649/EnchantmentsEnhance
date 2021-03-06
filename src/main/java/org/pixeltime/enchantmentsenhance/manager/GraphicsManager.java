/*
 *     Copyright (C) 2017-Present HealPotion
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.pixeltime.enchantmentsenhance.manager;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.ParticleEffect;
import de.slikey.effectlib.util.VectorUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.pixeltime.enchantmentsenhance.Main;

public class GraphicsManager {
    public EffectManager em;


    public void test(Player p) {
        em = new EffectManager(Main.getMain());
        final B a = new B(em);
        a.setEntity(p);
        a.start();
    }


    public class B extends Effect {
        public ParticleEffect particle;
        public int particles;
        public int particlesPerIteration;
        public float size;
        public float xFactor;
        public float yFactor;
        public float zFactor;
        public float xOffset;
        public float yOffset;
        public float zOffset;
        public double xRotation;
        public double yRotation;
        public double zRotation;
        protected int step;


        public B(final EffectManager effectManager) {
            super(effectManager);
            this.particle = ParticleEffect.SMOKE_LARGE;
            this.particles = 200;
            this.particlesPerIteration = 10;
            this.size = 1.0f;
            this.xFactor = 1.0f;
            this.yFactor = -1.8f;
            this.zFactor = 1.0f;
            this.yOffset = 0.5f;
            this.zRotation = 0.0;
            this.step = 0;
            this.type = EffectType.REPEATING;
            this.iterations = 9999999;
            this.period = 1;
        }


        public void reset() {
            this.step = 0;
        }


        public void onRun() {
            final Vector vector = new Vector();
            final Location location = this.getLocation();
            for (int i = 0; i < this.particlesPerIteration; ++i) {
                ++this.step;
                final float t = 1.1415927f / this.particles * this.step;
                final float r = MathUtils.sin(t) * this.size;
                final float s = 6.2831855f * t;
                vector.setX(this.xFactor * r * MathUtils.cos(s) + this.xOffset);
                vector.setZ(this.zFactor * r * MathUtils.sin(s) + this.zOffset);
                vector.setY(this.yFactor * this.size * MathUtils.cos(t)
                        + this.yOffset);
                VectorUtils.rotateVector(vector, this.xRotation, this.yRotation,
                        this.zRotation);
                this.display(this.particle, location.add(vector));
                location.subtract(vector);
            }
        }
    }
}
