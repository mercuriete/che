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
package org.eclipse.che.ide.ui.smartTree;

import org.eclipse.che.ide.api.project.node.Node;

import javax.validation.constraints.NotNull;

/**
 * Current ID provider is responsible for providing a unique identification for a specified node.
 *
 * @author Vlad Zhukovskyi
 */
public interface NodeUniqueKeyProvider extends UniqueKeyProvider<Node> {
    /** {@inheritDoc} */
    @NotNull
    String getKey(@NotNull Node item);
}
