/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ant.internal.ui.preferences;


import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.TextEditorPreferenceConstants;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * Preference constants used for the Ant Editor
 */
public class AntEditorPreferenceConstants {

	private AntEditorPreferenceConstants() {
	}
	
	/**
	 * A named preference that controls if the Ant Editor code assist gets auto activated.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION= "content_assist_autoactivation"; //$NON-NLS-1$

	/**
	 * A name preference that holds the auto activation delay time in milli seconds.
	 * <p>
	 * Value is of type <code>Int</code>.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION_DELAY= "content_assist_autoactivation_delay"; //$NON-NLS-1$

	/**
	 * A named preference that controls if the Ant editor code assist inserts a
	 * proposal automatically if only one proposal is available.
	 * <p>
	 * Value is of type <code>Boolean</code>.
	 * </p>
	 * @since 2.1
	 */
	public final static String CODEASSIST_AUTOINSERT= "content_assist_autoinsert"; //$NON-NLS-1$

	/**
	 * A named preference that holds the characters that auto activate code assist in Java code.
	 * <p>
	 * Value is of type <code>Sring</code>. All characters that trigger auto code assist in Java code.
	 * </p>
	 */
	public final static String CODEASSIST_AUTOACTIVATION_TRIGGERS= "content_assist_autoactivation_triggers_java"; //$NON-NLS-1$

	/**
	 * The symbolic names for colors for displaying code assist proposals
	 * @see org.eclipse.jface.resource.ColorRegistry
	 */
	public final static String CODEASSIST_PROPOSALS_BACKGROUND= "org.eclipse.ant.ui.codeAssistProposalsBackgroundColor"; //$NON-NLS-1$
	public final static String CODEASSIST_PROPOSALS_FOREGROUND= "org.eclipse.ant.ui.codeAssistProposalsForegroundColor"; //$NON-NLS-1$		
	public static final String CURRENT_LINE_COLOR = "org.eclipse.ant.ui.currentLineHightlightColor"; //$NON-NLS-1$
	public static final String LINE_NUMBER_RULER_COLOR = "org.eclipse.ant.ui.lineNumberForegroundColor"; //$NON-NLS-1$
	public static final String PRINT_MARGIN_COLOR = "org.eclipse.ant.ui.printMarginColor"; //$NON-NLS-1$	
	
	
	/**
	 * A named preference that specifies if the editor uses spaces for tabs.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code>spaces instead of tabs are used
	 * in the editor. If <code>false</code> the editor inserts a tab character when pressing the tab
	 * key.
	 * </p>
	 */
	public final static String EDITOR_SPACES_FOR_TABS= "spaces_for_tabs"; //$NON-NLS-1$
	
	/**
	 * A named preference that specifies the tab size for the Ant formatter.
	 * <p>
	 * Value is of type <code>int</code>.
	 * </p>
	 */
	public static final String FORMATTER_TAB_SIZE= "formatter_tab_size"; //$NON-NLS-1$
	
	/**
	 * A named preference that specifies if the Ant formatter uses spaces for tabs.
	 * <p>
	 * Value is of type <code>boolean</code>. If <code>false</code> spaces instead of tabs are used
	 * when formatting. If <code>true</code> the formatter inserts a tab character for indenting.
	 * </p>
	 */
	public static String FORMATTER_TAB_CHAR= "formatter_tab_char"; //$NON-NLS-1$
	
	/**
	 * A named preference that specifies if the Ant formatter aligns the final
	 * &quote&gt&quote in multi-line element tags
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> the final
	 * &quote&gt&quote in multi-line element tags are aligned by the formatter.
	 * </p>
	 */
	public static final String FORMATTER_ALIGN= "formatter_align"; //$NON-NLS-1$
	
	/**
	 * A named preference that specifies the maximum line length for the Ant formatter.
	 * <p>
	 * Value is of type <code>int</code>.
	 * </p>
	 */
	public static final String FORMATTER_MAX_LINE_LENGTH= "formatter_max_line_length"; //$NON-NLS-1$
			
	/**
	 * A named preference that specifies if the Ant formatter should wrap elements that are longer than
	 * the maximum line length.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> long elements are wrapped
	 * when formatting in the editor.
	 * </p>
	 */
	public static final String FORMATTER_WRAP_LONG= "formatter_wrap_long"; //$NON-NLS-1$
	
	/**
	 * Boolean preference identifier constant which specifies whether the Ant editor should
	 * format templates on insert.
	 */
	public static final String TEMPLATES_USE_CODEFORMATTER= "templates_use_codeformatter"; //$NON-NLS-1$

	/**
	 * Preference key suffix for bold text style preference keys.
	 * 
	 */
	public static final String EDITOR_BOLD_SUFFIX= "_bold"; //$NON-NLS-1$

	/**
	 * Preference key suffix for italic text style preference keys.
	 * 
	 */
	public static final String EDITOR_ITALIC_SUFFIX= "_italic"; //$NON-NLS-1$

	public static void initializeDefaultValues(IPreferenceStore store) {
		TextEditorPreferenceConstants.initializeDefaultValues(store);
		store.setDefault(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT, true);
		store.setDefault(AntEditorPreferenceConstants.CODEASSIST_AUTOINSERT, true);
		store.setDefault(AntEditorPreferenceConstants.CODEASSIST_AUTOACTIVATION, true);
		store.setDefault(AntEditorPreferenceConstants.CODEASSIST_AUTOACTIVATION_DELAY, 500);
		PreferenceConverter.setDefault(store, AntEditorPreferenceConstants.CODEASSIST_PROPOSALS_BACKGROUND, new RGB(254, 241, 233));
		PreferenceConverter.setDefault(store, AntEditorPreferenceConstants.CODEASSIST_PROPOSALS_FOREGROUND, new RGB(0, 0, 0));
		store.setDefault(AntEditorPreferenceConstants.CODEASSIST_AUTOACTIVATION_TRIGGERS, "<${"); //$NON-NLS-1$
		
		store.setDefault(AntEditorPreferenceConstants.EDITOR_SPACES_FOR_TABS, false);
		
		store.setDefault(AntEditorPreferenceConstants.FORMATTER_TAB_CHAR, true);
		store.setDefault(AntEditorPreferenceConstants.FORMATTER_TAB_SIZE, 4);
		store.setDefault(AntEditorPreferenceConstants.FORMATTER_ALIGN, false);
		store.setDefault(AntEditorPreferenceConstants.FORMATTER_MAX_LINE_LENGTH, 80);
		store.setDefault(AntEditorPreferenceConstants.FORMATTER_WRAP_LONG, false);
		
		store.setDefault(AntEditorPreferenceConstants.TEMPLATES_USE_CODEFORMATTER, true);
	}
}