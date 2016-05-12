/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.java.client.project.classpath.valueproviders.selectnode.interceptors;

import com.google.inject.Singleton;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.js.Promises;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.ide.ext.java.shared.ClasspathEntryKind;
import org.eclipse.che.ide.project.node.FolderReferenceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Interceptor for showing only folder nodes.
 *
 * @author Valeriy Svydenko
 */
@Singleton
public class ClassFolderNodeInterceptor implements ClasspathNodeInterceptor {
    @Override
    public Promise<List<Node>> intercept(Node parent, List<Node> children) {
        List<Node> nodes = new ArrayList<>();

        for (Node child : children) {
            if (!child.isLeaf()) {
                nodes.add(child);
            }
        }

        return Promises.resolve(nodes);
    }

    @Override
    public int getPriority() {
        return NORM_PRIORITY;
    }

    @Override
    public boolean isNodeValid(Node node) {
        return node instanceof FolderReferenceNode;
    }

    @Override
    public int getKind() {
        return ClasspathEntryKind.LIBRARY;
    }
}