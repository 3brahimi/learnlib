/* Copyright (C) 2013-2022 TU Dortmund
 * This file is part of LearnLib, http://www.learnlib.de/.
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
package de.learnlib.datastructure.pta.pta;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class AbstractBlueFringePTA<SP, TP, S extends AbstractBlueFringePTAState<SP, TP, S>>
        extends BasePTA<SP, TP, S> {

    protected final List<S> redStates = new ArrayList<>();

    public AbstractBlueFringePTA(@NonNegative int alphabetSize, S root) {
        super(alphabetSize, root);
    }

    public S getRedState(@NonNegative int id) {
        return redStates.get(id);
    }

    public @NonNegative int getNumRedStates() {
        return redStates.size();
    }

    public List<S> getRedStates() {
        return Collections.unmodifiableList(redStates);
    }

    public void init(Consumer<? super PTATransition<S>> newBlue) {
        root.color = Color.BLUE;
        promote(root, newBlue);
    }

    public void promote(S qb, Consumer<? super PTATransition<S>> newBlue) {
        makeRed(qb);
        qb.forEachSucc(s -> newBlue.accept(s.makeBlue()));
    }

    private void makeRed(S qb) {
        if (!qb.isBlue()) {
            throw new IllegalArgumentException();
        }
        qb.makeRed(redStates.size());
        redStates.add(qb);
    }

    public @Nullable RedBlueMerge<SP, TP, S> tryMerge(S qr, S qb) {
        RedBlueMerge<SP, TP, S> merge = new RedBlueMerge<>(this, qr, qb);
        if (!merge.merge()) {
            return null;
        }
        return merge;
    }

}
