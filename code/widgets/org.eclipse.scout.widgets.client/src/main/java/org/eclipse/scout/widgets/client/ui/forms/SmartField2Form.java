/*******************************************************************************
 * Copyright (c) 2015 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Distribution License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/edl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipse.scout.widgets.client.ui.forms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.columns.ColumnDescriptor;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCloseButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield2.AbstractProposalField2;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield2.AbstractSmartField2;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield2.ISmartField2;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.classid.ClassId;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.SleepUtil;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.widgets.client.services.lookup.AbstractLocaleLookupCall.LocaleTableRowData;
import org.eclipse.scout.widgets.client.services.lookup.HierarchicalLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.LocaleLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.RemoteLocaleLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.UserContentListLookupCall;
import org.eclipse.scout.widgets.client.services.lookup.UserContentTreeLookupCall;
import org.eclipse.scout.widgets.client.ui.desktop.outlines.IAdvancedExampleForm;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.CloseButton;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.BrowseAutoExpandAllField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.BrowseHierarchyField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.BrowseMaxRowCountField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.GetValueField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.ListEntriesField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.ListSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.TreeEntriesField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ConfigurationBox.TreeSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.ProposalFieldWithListContentBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.ProposalFieldWithListContentBox.DefaultProposalField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.ProposalFieldWithListContentBox.MandatoryProposalField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithListGroupBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithListGroupBox.DefaultField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithListGroupBox.DisabledField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithListGroupBox.MandatoryField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithListGroupBox.SmartFieldWithCustomTableField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.DefaultSmartField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.DisabledSmartFieldField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.ExamplesBox.SmartFieldWithTreeGroupBox.MandatorySmartfieldField;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.SampleContentButton;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.SeleniumTestMenu.SwitchLookupCallMenu;
import org.eclipse.scout.widgets.client.ui.forms.SmartField2Form.MainBox.SeleniumTestMenu.SwitchValidateValueMenu;
import org.eclipse.scout.widgets.client.ui.template.formfield.AbstractUserTreeField;
import org.eclipse.scout.widgets.shared.services.code.ColorsCodeType;
import org.eclipse.scout.widgets.shared.services.code.EventTypeCodeType;
import org.eclipse.scout.widgets.shared.services.code.IndustryICBCodeType;
import org.eclipse.scout.widgets.shared.services.code.IndustryICBCodeType.ICB9000.ICB9500.ICB9530.ICB9537;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Order(3000.0)
public class SmartField2Form extends AbstractForm implements IAdvancedExampleForm {

  private static final Logger LOG = LoggerFactory.getLogger(SmartField2Form.class);
  private static final Locale ALBANIAN = new Locale("sq");

  private boolean m_localLookupCall = true;
  private boolean m_validateValue = false;

  public SmartField2Form() {
    super();
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("SmartField2");
  }

  @Override
  public void startPageForm() {
    startInternal(new PageFormHandler());
  }

  /**
   * @return the BrowseAutoExpandAllField
   */
  public BrowseAutoExpandAllField getBrowseAutoExpandAllField() {
    return getFieldByClass(BrowseAutoExpandAllField.class);
  }

  /**
   * @return the BrowseHierarchyField
   */
  public BrowseHierarchyField getBrowseHierarchyField() {
    return getFieldByClass(BrowseHierarchyField.class);
  }

  /**
   * @return the BrowseMaxRowCountField
   */
  public BrowseMaxRowCountField getBrowseMaxRowCountField() {
    return getFieldByClass(BrowseMaxRowCountField.class);
  }

  @Override
  public CloseButton getCloseButton() {
    return getFieldByClass(CloseButton.class);
  }

  /**
   * @return the DisabledSmartFieldField
   */
  public DisabledSmartFieldField getDisabledSmartFieldField() {
    return getFieldByClass(DisabledSmartFieldField.class);
  }

  public ExamplesBox getExamplesBox() {
    return getFieldByClass(ExamplesBox.class);
  }

  /**
   * @return the DefaultField
   */
  public DefaultField getDefaultField() {
    return getFieldByClass(DefaultField.class);
  }

  public DisabledField getDisabledField() {
    return getFieldByClass(DisabledField.class);
  }

  /**
   * @return the MandatoryField
   */
  public MandatoryField getMandatoryField() {
    return getFieldByClass(MandatoryField.class);
  }

  /**
   * @return the MandatorySmartfieldField
   */
  public MandatorySmartfieldField getMandatorySmartfieldField() {
    return getFieldByClass(MandatorySmartfieldField.class);
  }

  public ConfigurationBox getConfigurationBox() {
    return getFieldByClass(ConfigurationBox.class);
  }

  /**
   * @return the DefaultSmartField
   */
  public DefaultSmartField getDefaultSmartField() {
    return getFieldByClass(DefaultSmartField.class);
  }

  public DefaultProposalField getDefaultProposalField() {
    return getFieldByClass(DefaultProposalField.class);
  }

  public MandatoryProposalField getMandatoryProposalField() {
    return getFieldByClass(MandatoryProposalField.class);
  }

  /**
   * @return the GetCheckedKeysField
   */
  public GetValueField getGetValueField() {
    return getFieldByClass(GetValueField.class);
  }

  /**
   * @return the ListSmartField
   */
  public ListSmartField getListSmartField() {
    return getFieldByClass(ListSmartField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  /**
   * @return the TreeEntriesField
   */
  public ListEntriesField getListEntriesField() {
    return getFieldByClass(ListEntriesField.class);
  }

  /**
   * @return the TreeEntriesField
   */
  public TreeEntriesField getTreeEntriesField() {
    return getFieldByClass(TreeEntriesField.class);
  }

  public SmartFieldWithCustomTableField getSmartFieldWithCustomTableField() {
    return getFieldByClass(SmartFieldWithCustomTableField.class);
  }

  public SmartFieldWithListGroupBox getSmartFieldWithListGroupBox() {
    return getFieldByClass(SmartFieldWithListGroupBox.class);
  }

  public SmartFieldWithTreeGroupBox getSmartFieldWithTreeGroupBox() {
    return getFieldByClass(SmartFieldWithTreeGroupBox.class);
  }

  public ProposalFieldWithListContentBox getProposalFieldWithListContentBox() {
    return getFieldByClass(ProposalFieldWithListContentBox.class);
  }

  /**
   * @return the SampleContentButton
   */
  public SampleContentButton getSampleContentButton() {
    return getFieldByClass(SampleContentButton.class);
  }

  /**
   * @return the TreeSmartField
   */
  public TreeSmartField getTreeSmartField() {
    return getFieldByClass(TreeSmartField.class);
  }

  @Order(10)
  public class MainBox extends AbstractGroupBox {

    @Order(10)
    public class ExamplesBox extends AbstractGroupBox {

      @Override
      protected int getConfiguredGridColumnCount() {
        return 3;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Examples");
      }

      @Order(1000)
      @ClassId("d7594941-dad5-4ce2-af9d-e5a9950d2303")
      public class SmartFieldWithListGroupBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("SmartFieldWithListContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(30)
        public class DefaultField extends AbstractSmartField2<Locale> {

          private boolean m_throwVetoException = false;

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }

          @Override
          protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
            return (Class<? extends ILookupCall<Locale>>) /*Remote*/ LocaleLookupCall.class;
          }

          @Override
          protected void execPrepareLookup(ILookupCall<Locale> call) {
            if (call instanceof LocaleLookupCall) { // for some tests the lookup class is changed dynamically
              ((LocaleLookupCall) call).setThrowVetoException(m_throwVetoException);
            }
          }

          @Override
          protected Locale execValidateValue(Locale rawValue) {
            Locale validValue = rawValue;
            if (m_validateValue) {
              validValue = ALBANIAN;
            }
            return validValue;
          }

          public void setThrowVetoException(boolean throwVetoException) {
            m_throwVetoException = throwVetoException;
          }

          public boolean isThrowVetoException() {
            return m_throwVetoException;
          }

        }

        @Order(40)
        public class MandatoryField extends AbstractSmartField2<Color> {

          @Override
          protected Class<? extends ICodeType<?, Color>> getConfiguredCodeType() {
            return ColorsCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }

          @Override
          protected void execChangedValue() {
            Color color = getValue();
            if (color == null) {
              setBackgroundColor(null);
            }
            else {
              String hex = Integer.toHexString(color.getRGB()).substring(2);
              setBackgroundColor(hex);
            }
          }
        }

        @Order(50)
        public class DisabledField extends AbstractSmartField2<Color> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Color>> getConfiguredCodeType() {
            return ColorsCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValue(new Color(255, 255, 255));
          }
        }

        @Order(55)
        @ClassId("75eed6c0-aee7-4632-80ec-e4768c9c4bdb")
        public class SmartFieldWithCustomTableField extends AbstractSmartField2<Locale> {
          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("CustomTable");
          }

          @Override
          protected Class<? extends ILookupCall<Locale>> getConfiguredLookupCall() {
            return (Class<? extends ILookupCall<Locale>>) /*Remote*/LocaleLookupCall.class;
          }

          @Override
          protected ColumnDescriptor[] getConfiguredColumnDescriptors() {
            return new ColumnDescriptor[]{
                new ColumnDescriptor(null, "Text", 200),
                new ColumnDescriptor(LocaleTableRowData.country, TEXTS.get("Country"), 90),
                new ColumnDescriptor(LocaleTableRowData.language, TEXTS.get("Language"), 90)
            };
          }
        }

        @Order(60)
        @ClassId("96674af1-a590-42e6-985c-6adb210e8504")
        public class SmartFieldDisplayStyleField extends AbstractBooleanField {
          @Override
          protected String getConfiguredLabel() {
            return "Dropdown";
          }

          protected boolean isDropdown() {
            return ISmartField2.DISPLAY_STYLE_DROPDOWN.equals(getDefaultField().getDisplayStyle());
          }

          @Override
          protected void execInitField() {
            setValue(isDropdown());
          }

          @Override
          protected void execChangedValue() {
            getDefaultField().setDisplayStyle(getValue() ? ISmartField2.DISPLAY_STYLE_DROPDOWN : ISmartField2.DISPLAY_STYLE_DEFAULT);
          }
        }
      }

      @Order(2000)
      @ClassId("ce63629e-21ca-4d0c-aeeb-915aaf650636")
      public class SmartFieldWithTreeGroupBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("SmartFieldWithTreeContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(70)
        public class DefaultSmartField extends AbstractSmartField2<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }

          @Override
          protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
            return HierarchicalLookupCall.class;
          }

          @Override
          protected boolean getConfiguredBrowseHierarchy() {
            return true;
          }

          @Override
          protected boolean getConfiguredBrowseLoadIncremental() {
            return true;
          }

          @Override
          protected void execPrepareBrowseLookup(ILookupCall<Long> call, String browseHint) {
            // instanceof is required because in this form the lookup can be switched at runtime
            if (call instanceof HierarchicalLookupCall) {
              ((HierarchicalLookupCall) call).setLoadIncremental(isBrowseLoadIncremental());
            }
          }
        }

        @Order(80)
        public class MandatorySmartfieldField extends AbstractSmartField2<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return IndustryICBCodeType.class;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(90)
        public class DisabledSmartFieldField extends AbstractSmartField2<Long> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return IndustryICBCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValue(ICB9537.ID);
          }
        }

        @Order(100)
        public class LoadIncrementalField extends AbstractBooleanField {

          @Override
          protected String getConfiguredLabel() {
            return "Load incremental";
          }

          @Override
          protected void execInitField() {
            setValue(getDefaultSmartField().isBrowseLoadIncremental());
          }

          @Override
          protected void execChangedValue() {
            getDefaultSmartField().setBrowseLoadIncremental(getValue());
          }
        }
      }

      @Order(3000)
      @ClassId("2a8481a4-6fcc-446d-8d10-384ac37cb739")
      public class ProposalFieldWithListContentBox extends AbstractGroupBox {
        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ProposalFieldWithListContent");
        }

        @Override
        protected int getConfiguredGridColumnCount() {
          return 1;
        }

        @Override
        protected int getConfiguredGridW() {
          return 1;
        }

        @Order(110)
        public class DefaultProposalField extends AbstractProposalField2<Long> {

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Default");
          }
        }

        @Order(120)
        public class MandatoryProposalField extends AbstractProposalField2<Long> {

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Mandatory");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(130)
        public class DisabledProposalField extends AbstractProposalField2<Long> {

          @Override
          protected boolean getConfiguredEnabled() {
            return false;
          }

          @Override
          protected String getConfiguredLabel() {
            return TEXTS.get("Disabled");
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return EventTypeCodeType.class;
          }

          @Override
          protected void execInitField() {
            setValueAsString(TEXTS.get("Public"));
          }
        }
      }

    }

    @Order(20)
    public class ConfigurationBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Configure");
      }

      @Override
      protected int getConfiguredGridColumnCount() {
        return 3;
      }

      @Order(10)
      public class ListSmartField extends AbstractSmartField2<String> {

        private boolean m_throttled = false;

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ListSmartField");
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return (Class<? extends ILookupCall<String>>) UserContentListLookupCall.class;
        }

        @Override
        protected void execPrepareLookup(ILookupCall<String> call) {
          if (m_throttled) {
            SleepUtil.sleepSafe(1, TimeUnit.SECONDS);
          }
          super.execPrepareLookup(call);
        }

        @Override
        protected void execChangedValue() {
          if (m_throttled) {
            SleepUtil.sleepSafe(1, TimeUnit.SECONDS);
          }
          super.execChangedValue();
        }

        public boolean toggleThrottle() {
          m_throttled = !m_throttled;
          return m_throttled;
        }

      }

      @Order(20)
      public class GetValueField extends AbstractStringField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("GetValue");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return SmartField2Form.MainBox.ConfigurationBox.ListSmartField.class;
        }

        @Override
        protected void execChangedMasterValue(Object newMasterValue) {
          if (newMasterValue != null) {
            setValue(newMasterValue.toString());
          }
          else {
            setValue(null);
          }
        }
      }

      @Order(30)
      public class ListEntriesField extends AbstractUserTreeField {

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("ListContent");
        }

        @Override
        protected void execChangedValue() {
          List<Node> nodes = parseFieldValue(false);
          ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();
          addNodesToLookupRows(nodes, rows);

          ((UserContentListLookupCall) getListSmartField().getLookupCall()).setLookupRows(rows);
          getListSmartField().initField();
        }
      }

      @Order(40)
      public class BrowseMaxRowCountField extends AbstractIntegerField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseMaxRowCount");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected Integer getConfiguredMinValue() {
          return 0;
        }

        @Override
        protected void execChangedValue() {
          getListSmartField().setBrowseMaxRowCount(NumberUtility.nvl(getValue(), 100));
          getTreeSmartField().setBrowseMaxRowCount(NumberUtility.nvl(getValue(), 100));
        }
      }

      @Order(50)
      public class EnableActiveFilterField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("EnableActiveFilter");
        }

        @Override
        protected void execChangedValue() {
          getListSmartField().setActiveFilterEnabled(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getListSmartField().isActiveFilterEnabled());
        }
      }

      @Order(70)
      public class TreeSmartField extends AbstractSmartField2<String> {

        @Override
        protected boolean getConfiguredBrowseAutoExpandAll() {
          return false;
        }

        @Override
        protected boolean getConfiguredBrowseHierarchy() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("TreeSmartField");
        }

        @Override
        protected Class<? extends ILookupCall<String>> getConfiguredLookupCall() {
          return (Class<? extends ILookupCall<String>>) UserContentTreeLookupCall.class;
        }
      }

      @Order(80)
      public class GetValueTreeSmartFieldField extends AbstractStringField {

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("GetValue");
        }

        @Override
        protected Class<? extends IValueField> getConfiguredMasterField() {
          return SmartField2Form.MainBox.ConfigurationBox.TreeSmartField.class;
        }

        @Override
        protected void execChangedMasterValue(Object newMasterValue) {
          if (newMasterValue != null) {
            setValue(newMasterValue.toString());
          }
          else {
            setValue(null);
          }
        }
      }

      @Order(90)
      public class TreeEntriesField extends AbstractUserTreeField {

        @Override
        protected int getConfiguredGridH() {
          return 3;
        }

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("TreeContent");
        }

        @Override
        protected void execChangedValue() {
          List<Node> nodes = parseFieldValue(true);
          List<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();

          addNodesToLookupRows(nodes, rows);
          ((UserContentTreeLookupCall) getTreeSmartField().getLookupCall()).setLookupRows(rows);
        }
      }

      @Order(100)
      public class BrowseHierarchyField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseHierarchy");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected void execChangedValue() {
          getTreeSmartField().setBrowseHierarchy(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getTreeSmartField().isBrowseHierarchy());
        }
      }

      @Order(110)
      public class BrowseAutoExpandAllField extends AbstractBooleanField {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("BrowseAutoExpandAll");
        }

        @Override
        protected String getConfiguredLabelFont() {
          return "ITALIC";
        }

        @Override
        protected void execChangedValue() {
          getTreeSmartField().setBrowseAutoExpandAll(getValue());
        }

        @Override
        protected void execInitField() {
          setValue(getTreeSmartField().isBrowseAutoExpandAll());
        }
      }

      @Order(120)
      public class PlaceholderField extends AbstractPlaceholderField {

        @Override
        protected int getConfiguredGridH() {
          return 6;
        }

      }
    }

    @Order(30)
    public class SampleContentButton extends AbstractButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SampleContent");
      }

      @Override
      protected void execClickAction() {
        ListEntriesField listEntries = getListEntriesField();
        listEntries.setValue(TEXTS.get("ListBoxUserContent"));

        TreeEntriesField treeEntries = getTreeEntriesField();
        treeEntries.setValue(TEXTS.get("TreeUserContent"));
      }
    }

    @Order(35)
    public class SeleniumTestMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("Selenium");
      }

      @Order(10)
      public class WildcardMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("Wildcard");
        }

        @Order(10)
        public class AutoPrefixWildcardMenu extends AbstractMenu {

          private boolean m_active;

          @Override
          protected void execInitAction() {
            m_active = ClientSessionProvider.currentSession().getDesktop().isAutoPrefixWildcardForTextSearch();
            updateText();
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("EnableAutoPrefixWildcard");
          }

          @Override
          protected void execAction() {
            m_active = !m_active;
            ClientSessionProvider.currentSession().getDesktop().setAutoPrefixWildcardForTextSearch(m_active);
            updateText();
          }

          private void updateText() {
            setText(TEXTS.get(m_active ? "DisableAutoPrefixWildcard" : "EnableAutoPrefixWildcard"));
          }
        }

        @Order(20)
        public class ChangeWildcard1Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("*");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "*");
          }
        }

        @Order(30)
        public class ChangeWildcard2Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard(".*");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", ".*");
          }
        }

        @Order(40)
        public class ChangeWildcard3Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("\\");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "\\");
          }
        }

        @Order(50)
        public class ChangeWildcard4Menu extends AbstractMenu {
          @Override
          protected void execAction() {
            changeWildcard("°");
          }

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("ChangeWildcard", "°");
          }
        }
      }

      @Order(20)
      public class ShowSmartFieldValueMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ShowValue", "Smart field");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ShowValueOfDefaultField", "Smart field");
        }

        @Override
        protected void execAction() {
          MessageBoxes.createOk().withBody(getDefaultField().getValue() + "").show();
        }

        @Override
        protected String getConfiguredKeyStroke() {
          return "Ctrl-Alt-P";
        }
      }

      @Order(30)
      public class ShowProposalFieldValueMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ShowValue", "Proposal field");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ShowValueOfDefaultField", "Proposal field");
        }

        @Override
        protected void execAction() {
          MessageBoxes.createOk().withBody(getDefaultProposalField().getValue() + "").show();
        }

        @Override
        protected String getConfiguredKeyStroke() {
          return "Ctrl-Alt-Q";
        }
      }

      @Order(40)
      public class SwitchLookupCallMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("SwitchToRemote");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("SwitchToRemoteTooltip");
        }

        @Override
        protected void execAction() {
          setLocalLookupCall(!m_localLookupCall);
        }
      }

      @Order(50)
      public class SeleniumResetMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("Reset");
        }

        @Override
        protected void execAction() {
          setLocalLookupCall(true);
          setValidateValue(false);
        }
      }

      @Order(60)
      public class ThrottleSmartFieldMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("ThrottleSmartFieldRequests");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ThrottleSmartFieldRequestsTooltip");
        }

        @Override
        protected void execAction() {
          boolean throttled = getListSmartField().toggleThrottle();
          if (throttled) {
            setText(TEXTS.get("NoThrottling"));
            setTooltipText(TEXTS.get("NoThrottlingSmartFieldRequestsTooltip"));
          }
          else {
            setText(TEXTS.get("ThrottleSmartFieldRequests"));
            setTooltipText(TEXTS.get("ThrottleSmartFieldRequestsTooltip"));
          }
        }
      }

      @Order(70)
      public class SwitchHierarchicalLookupMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return TEXTS.get("SwitchLookupCall", "HierarchicalLookupCall");
        }

        @Override
        protected String getConfiguredTooltipText() {
          return TEXTS.get("ThrottleSmartFieldRequestsTooltip");
        }

        @Override
        protected void execAction() {
          ILookupCall<Long> lookupCall = getDefaultSmartField().getLookupCall();
          getDefaultSmartField().setValue(null);
          if (lookupCall instanceof HierarchicalLookupCall) {
            setText(TEXTS.get("SwitchLookupCall", "HierarchicalLookupCall"));
            getDefaultSmartField().setCodeTypeClass(IndustryICBCodeType.class);
          }
          else {
            setText(TEXTS.get("SwitchLookupCall", "IndustryICBCodeType"));
            getDefaultSmartField().setLookupCall(BEANS.get(HierarchicalLookupCall.class));
          }
        }
      }

      @Order(80)
      public class SwitchExceptionOnLookupMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return "Throw VetoException on lookup";
        }

        @Override
        protected String getConfiguredTooltipText() {
          return "DefaultSmartField throws a VetoException on every lookup";
        }

        @Override
        protected void execAction() {
          boolean isThrow = getDefaultField().isThrowVetoException();
          if (isThrow) {
            setText("Throw VetoException on lookup");
          }
          else {
            setText("Don't throw VetoException on lookup");
          }
          getDefaultField().setThrowVetoException(!isThrow);
        }
      }

      @Order(90)
      public class SwitchValidateValueMenu extends AbstractMenu {

        @Override
        protected String getConfiguredText() {
          return "Set Locale 'ar' in validateValue";
        }

        @Override
        protected String getConfiguredTooltipText() {
          return "Always set the value to Locale 'ar' in validateValue";
        }

        @Override
        protected void execAction() {
          setValidateValue(!m_validateValue);
        }
      }

    }

    @Order(40)
    public class CloseButton extends AbstractCloseButton {
    }
  }

  public class PageFormHandler extends AbstractFormHandler {
  }

  public void setLocalLookupCall(boolean localLookupCall) {
    m_localLookupCall = localLookupCall;
    IMenu menu = getMainBox().getMenuByClass(SwitchLookupCallMenu.class);
    getDefaultField().setLookupCall(m_localLookupCall ? new LocaleLookupCall() : new RemoteLocaleLookupCall());
    menu.setText(TEXTS.get(m_localLookupCall ? "SwitchToRemote" : "SwitchToLocal"));
    menu.setTooltipText(TEXTS.get(m_localLookupCall ? "SwitchToRemoteTooltip" : "SwitchToLocalTooltip"));
    LOG.debug("Switched lookup-call of DefaultField to {} instance", (m_localLookupCall ? "local" : "remote"));
  }

  public void setValidateValue(boolean validateValue) {
    m_validateValue = validateValue;
    IMenu menu = getMainBox().getMenuByClass(SwitchValidateValueMenu.class);
    menu.setText(validateValue ? "Do nothing in validateValue" : "Set Locale 'ar' in validateValue");
  }

  private void changeWildcard(String wildcard) {
    getDefaultField().setWildcard(wildcard);
    getMandatoryField().setWildcard(wildcard);
    getDefaultSmartField().setWildcard(wildcard);
    getMandatorySmartfieldField().setWildcard(wildcard);
    getDefaultProposalField().setWildcard(wildcard);
    getMandatoryProposalField().setWildcard(wildcard);
    getListSmartField().setWildcard(wildcard);
    getTreeSmartField().setWildcard(wildcard);
  }

}
