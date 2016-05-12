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
package org.eclipse.che.ide.ext.java.client.tree.library;

import com.google.common.annotations.Beta;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.web.bindery.event.shared.EventBus;

import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.js.Promises;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.data.tree.HasAction;
import org.eclipse.che.ide.api.data.tree.Node;
import org.eclipse.che.ide.api.data.tree.settings.NodeSettings;
import org.eclipse.che.ide.api.event.FileEvent;
import org.eclipse.che.ide.api.project.HasProjectConfig;
import org.eclipse.che.ide.api.resources.VirtualFile;
import org.eclipse.che.ide.api.theme.Style;
import org.eclipse.che.ide.ext.java.client.JavaResources;
import org.eclipse.che.ide.ext.java.client.navigation.service.JavaNavigationService;
import org.eclipse.che.ide.ext.java.shared.JarEntry;
import org.eclipse.che.ide.ext.java.shared.dto.ClassContent;
import org.eclipse.che.ide.project.node.SyntheticNode;
import org.eclipse.che.ide.project.shared.NodesResources;
import org.eclipse.che.ide.resource.Path;
import org.eclipse.che.ide.ui.smartTree.presentation.NodePresentation;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.eclipse.che.ide.api.event.FileEvent.FileOperation.OPEN;

/**
 * It might be used for any jar content.
 *
 * @author Vlad Zhukovskiy
 */
@Beta
public class JarFileNode extends SyntheticNode<JarEntry> implements VirtualFile, HasAction {

    private final int                   libId;
    private final Path                  project;
    private final EventBus              eventBus;
    private final JavaResources         javaResources;
    private final NodesResources        nodesResources;
    private final JavaNavigationService service;

    @Inject
    public JarFileNode(@Assisted JarEntry jarEntry,
                       @Assisted int libId,
                       @Assisted Path project,
                       @Assisted NodeSettings nodeSettings,
                       EventBus eventBus,
                       JavaResources javaResources,
                       NodesResources nodesResources,
                       JavaNavigationService service) {
        super(jarEntry, nodeSettings);
        this.libId = libId;
        this.project = project;
        this.eventBus = eventBus;
        this.javaResources = javaResources;
        this.nodesResources = nodesResources;
        this.service = service;

        getAttributes().put(CUSTOM_BACKGROUND_FILL, singletonList(Style.theme.projectExplorerReadonlyItemBackground()));
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    protected Promise<List<Node>> getChildrenImpl() {
        return Promises.resolve(Collections.<Node>emptyList());
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed() {
        eventBus.fireEvent(new FileEvent(this, OPEN));
    }

    /** {@inheritDoc} */
    @Override
    public void updatePresentation(@NotNull NodePresentation presentation) {
        presentation.setPresentableText(getDisplayName());
        presentation.setPresentableIcon(isClassFile() ? javaResources.javaFile() : nodesResources.file());
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getName() {
        return getData().getName();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isLeaf() {
        return true;
    }

    /** {@inheritDoc} */
    @NotNull
    @Override
    public String getPath() {
        return getData().getPath();
    }

    @Override
    public Path getLocation() {
        return Path.valueOf(getPath());
    }

    /** {@inheritDoc} */
    @Override
    public String getDisplayName() {
        if (isClassFile()) {
            return getData().getName().substring(0, getData().getName().lastIndexOf(".class"));
        } else {
            return getData().getName();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isReadOnly() {
        return true;
    }

    /** {@inheritDoc} */
    @Nullable
    @Override
    public HasProjectConfig getProject() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getContentUrl() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Promise<String> getContent() {
        if (libId != -1) {
            return service.getContent(project, libId, Path.valueOf(getData().getPath())).then(new Function<ClassContent, String>() {
                @Override
                public String apply(ClassContent arg) throws FunctionException {
                    return arg.getContent();
                }
            });
        } else {
            return service.getContent(project, getData().getPath()).then(new Function<ClassContent, String>() {
                @Override
                public String apply(ClassContent arg) throws FunctionException {
                    return arg.getContent();
                }
            });
        }
    }

    /** {@inheritDoc} */
    @Override
    public Promise<Void> updateContent(String content) {
        throw new IllegalStateException("Update content on class file is not supported.");
    }

    private boolean isClassFile() {
        return getData().getName().endsWith(".class");
    }

    @Override
    public String getMediaType() {
        return null;
    }
}