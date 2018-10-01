/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.Components.JAttribute;
import AIToolkit.Bayes.Net.BayesNet;
import AIToolkit.Bayes.Net.BayesNetItem;
import AIToolkit.Bayes.Net.Predictor;
import AIToolkit.Bayes.Net.Sampling.LikelihoodWeightSampling;
import AIToolkit.Bayes.Net.Sampling.PriorSampling;
import AIToolkit.Bayes.Net.Sampling.RejectionSampling;
import AIToolkit.Bayes.Net.Sampling.SampleItem;
import AIToolkit.Bayes.Net.Sampling.Sampling;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBase;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseHeader;
import AIToolkit.Supervisioned.KnowledgeBase.KnowledgeBaseItem;
import app.Models.LineData;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Luan
 */
public class MainApp extends javax.swing.JFrame {

    private BayesNet bn;

    // UI MANAGEMENT
    private HashMap<String, JAttribute> attributeUI;
    private LineData cLine;
    private JAttribute cParentAttribute;

    // UI PREDICTING
    private Predictor predicting; // Holds the required predictor
    private BayesNetItem cPredictorSelected; // Holds the current selected item to be added to the predictor
    private HashMap<String, String> samplingValues; // Holds the current selection for sampling

    /**
     * Creates new form MainApp
     */
    public MainApp() {
        initComponents();

        attributeUI = new HashMap<>();
        samplingValues = new HashMap<>();
        predicting = new Predictor();

        listAttr.addListSelectionListener((ListSelectionEvent lse) -> {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            String attr = listAttr.getModel().getElementAt(listAttr.getSelectedIndex());
            ((TitledBorder) predictingValuePanel.getBorder()).setTitle("Value for " + attr);
            predictingValuePanel.repaint();

            if (attributeUI.get(attr) == null) {
                return;
            }

            HashSet<String> possibleValues = attributeUI.get(attr).getAttribute().getPossibleValues();
            String[] posValues = possibleValues.toArray(new String[possibleValues.size() + 1]);
            posValues[possibleValues.size()] = "NONE";
            cPredictorSelected = attributeUI.get(attr).getAttribute();

            cmbValue.setModel(new DefaultComboBoxModel<>(posValues));

            String selectedElement = predicting.getGivenThat().get(attributeUI.get(attr).getAttribute());
            cmbValue.setSelectedItem(selectedElement == null ? "NONE" : selectedElement);
        });
        listSamplingAttr.addListSelectionListener((ListSelectionEvent lse) -> {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            String attr = listSamplingAttr.getModel().getElementAt(listSamplingAttr.getSelectedIndex());
            ((TitledBorder) samplingValuePanel.getBorder()).setTitle("Value for " + attr);
            samplingValuePanel.repaint();

            if (attributeUI.get(attr) == null) {
                return;
            }

            HashSet<String> possibleValues = attributeUI.get(attr).getAttribute().getPossibleValues();
            String[] posValues = possibleValues.toArray(new String[possibleValues.size() + 1]);
            posValues[possibleValues.size()] = "NONE";

            String selectedElement = samplingValues.get(attr);
            cmbSamplingValue.setModel(new DefaultComboBoxModel<>(posValues));
            cmbSamplingValue.setSelectedItem(selectedElement == null ? "NONE" : selectedElement);
        });
        cmbValue.addActionListener((ActionEvent e) -> {
            String item = (String) cmbValue.getSelectedItem();

            if (item.equals("NONE")) {
                predicting.getGivenThat().remove(cPredictorSelected);
            } else {
                predicting.getGivenThat().put(cPredictorSelected, item);
            }
        });
        cmbSamplingValue.addActionListener((ActionEvent e) -> {
            String item = (String) cmbSamplingValue.getSelectedItem();

            if (item.equals("NONE")) {
                samplingValues.remove(listSamplingAttr.getSelectedValue());
            } else {
                samplingValues.put(listSamplingAttr.getSelectedValue(), item);
            }
        });
        GraphContainerScroll.getVerticalScrollBar().setUnitIncrement(16);
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(getClass().getResource("/app/Resources/loading.gif")).getImage().getScaledInstance(46, 46, Image.SCALE_DEFAULT));
        loading.setIcon(imageIcon);

        hideLoading("Select a dataset to start.");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuLoad = new javax.swing.JPopupMenu();
        btnLoadDataset = new javax.swing.JMenuItem();
        btnLoadNetwork = new javax.swing.JMenuItem();
        parentRemover = new javax.swing.JPopupMenu();
        tab = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel5 = new javax.swing.JPanel();
        btnTest = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        txtCorrects = new javax.swing.JTextField();
        txtWrong = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        txtPrecision = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbClassPrecision = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbAttributeToPredict = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        listAttr = new javax.swing.JList<>();
        predictingValuePanel = new javax.swing.JPanel();
        cmbValue = new javax.swing.JComboBox<>();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPredictedClass = new javax.swing.JTextField();
        btnPredict = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        cmbSamplingAlgorithm = new javax.swing.JComboBox<>();
        btnSampling = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSamplingN = new javax.swing.JSpinner();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        listSamplingAttr = new javax.swing.JList<>();
        samplingValuePanel = new javax.swing.JPanel();
        cmbSamplingValue = new javax.swing.JComboBox<>();
        jSeparator9 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        cmbQueryAttr = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtSamplingResults = new javax.swing.JTextArea();
        GraphContainerScroll = new javax.swing.JScrollPane();
        GraphContainer = new app.Components.GraphContainer();
        jPanel8 = new javax.swing.JPanel();
        loading = new javax.swing.JLabel();
        txtStatus = new javax.swing.JLabel();

        btnLoadDataset.setText("Load dataset");
        btnLoadDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadDatasetActionPerformed(evt);
            }
        });
        menuLoad.add(btnLoadDataset);

        btnLoadNetwork.setText("Load network and dataset");
        btnLoadNetwork.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadNetworkActionPerformed(evt);
            }
        });
        menuLoad.add(btnLoadNetwork);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(874, 474));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Check network accuracy"));

        btnTest.setText("Run test");
        btnTest.setToolTipText("<html>\nPart 2 of the project is to run prediction and verify the precision<br>\nof the network. <b>Click this button</b> to select the file, the class to be predicted<br>\nand run the tests.");
        btnTest.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTestMouseClicked(evt);
            }
        });
        btnTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestActionPerformed(evt);
            }
        });

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setText("Correct:");

        txtCorrects.setEditable(false);

        txtWrong.setEditable(false);

        jLabel4.setText("Incorrect:");

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel5.setText("Accuracy:");

        txtPrecision.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecision.setText("_");

        jLabel6.setText("Class:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(btnTest)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(cmbClassPrecision, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(txtCorrects, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(txtWrong, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(txtPrecision))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnTest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator4)
            .addComponent(jSeparator5)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtPrecision))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCorrects, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtWrong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(cmbClassPrecision))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("File"));

        btnLoad.setText("Load...");
        btnLoad.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLoadMouseClicked(evt);
            }
        });
        btnLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSaveMouseClicked(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLoad, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnLoad, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(165, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tab.addTab("Network", jPanel3);

        jLabel1.setText("Predict:");

        cmbAttributeToPredict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAttributeToPredictActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        listAttr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(listAttr);

        predictingValuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Value"));

        javax.swing.GroupLayout predictingValuePanelLayout = new javax.swing.GroupLayout(predictingValuePanel);
        predictingValuePanel.setLayout(predictingValuePanelLayout);
        predictingValuePanelLayout.setHorizontalGroup(
            predictingValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(predictingValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbValue, 0, 214, Short.MAX_VALUE)
                .addContainerGap())
        );
        predictingValuePanelLayout.setVerticalGroup(
            predictingValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(predictingValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Predicted class"));
        jPanel4.setToolTipText("");

        jLabel2.setText("Predicted:");

        txtPredictedClass.setEditable(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(txtPredictedClass)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(2, 2, 2)
                .addComponent(txtPredictedClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnPredict.setText("PREDICT");
        btnPredict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPredictActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbAttributeToPredict, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnPredict, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(predictingValuePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(cmbAttributeToPredict, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPredict, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(predictingValuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tab.addTab("Predict (manual)", jPanel2);

        cmbSamplingAlgorithm.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Prior Sampling", "Rejection Sampling", "Likelihood Weighting" }));

        btnSampling.setText("Generate sample");
        btnSampling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSamplingActionPerformed(evt);
            }
        });

        jLabel7.setText("Sample size:");

        txtSamplingN.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1000));

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator8.setOrientation(javax.swing.SwingConstants.VERTICAL);

        listSamplingAttr.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listSamplingAttr);

        samplingValuePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Value"));

        javax.swing.GroupLayout samplingValuePanelLayout = new javax.swing.GroupLayout(samplingValuePanel);
        samplingValuePanel.setLayout(samplingValuePanelLayout);
        samplingValuePanelLayout.setHorizontalGroup(
            samplingValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(samplingValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbSamplingValue, 0, 136, Short.MAX_VALUE)
                .addContainerGap())
        );
        samplingValuePanelLayout.setVerticalGroup(
            samplingValuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(samplingValuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbSamplingValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Query for"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbQueryAttr, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmbQueryAttr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Inference results:"));

        txtSamplingResults.setEditable(false);
        txtSamplingResults.setColumns(20);
        txtSamplingResults.setRows(5);
        jScrollPane3.setViewportView(txtSamplingResults);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbSamplingAlgorithm, 0, 173, Short.MAX_VALUE)
                    .addComponent(btnSampling, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(txtSamplingN, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(samplingValuePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator7)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cmbSamplingAlgorithm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSampling, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator8)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator9)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(samplingValuePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(3, 3, 3)
                                .addComponent(txtSamplingN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tab.addTab("Inference (sampling)", jPanel6);

        javax.swing.GroupLayout GraphContainerLayout = new javax.swing.GroupLayout(GraphContainer);
        GraphContainer.setLayout(GraphContainerLayout);
        GraphContainerLayout.setHorizontalGroup(
            GraphContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        GraphContainerLayout.setVerticalGroup(
            GraphContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        GraphContainerScroll.setViewportView(GraphContainer);

        jPanel8.setMaximumSize(new java.awt.Dimension(109, 46));
        jPanel8.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanel8.setPreferredSize(new java.awt.Dimension(109, 46));

        loading.setIcon(new javax.swing.ImageIcon(getClass().getResource("/app/Resources/loading.gif"))); // NOI18N
        loading.setMaximumSize(new java.awt.Dimension(36, 36));
        loading.setMinimumSize(new java.awt.Dimension(36, 36));
        loading.setPreferredSize(new java.awt.Dimension(36, 36));

        txtStatus.setText("Load the dataset to start.");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loading, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loading, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtStatus)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(GraphContainerScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(tab))
                .addContainerGap())
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 886, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GraphContainerScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadActionPerformed

    }//GEN-LAST:event_btnLoadActionPerformed

    private void btnLoadMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLoadMouseClicked
        menuLoad.show(evt.getComponent(),
                evt.getX(),
                evt.getY()
        );
    }//GEN-LAST:event_btnLoadMouseClicked

    private void btnLoadDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadDatasetActionPerformed
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            new Thread(() -> {
                try {
                    loadDataset(fc.getSelectedFile());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                hideLoading("Dataset \"" + fc.getSelectedFile().getName() + "\" has been loaded correctly.");
            }).start();
        }
    }//GEN-LAST:event_btnLoadDatasetActionPerformed

    private void loadDataset(File file) throws Exception {
        attributeUI.clear();
        GraphContainer.removeAll();
        GraphContainer.clearArrows();

        setLoading("Loading " + file.getName() + " dataset.");
        KnowledgeBase<String> dataset = KnowledgeBase.builder()
                .fromFile(file)
                .savingHeader(true)
                .withClassNameIn(-2)
                .withExampleNameIn(-2)
                .withSeparator(",")
                .build();

        ArrayList<String> cmbAttr = new ArrayList<>();
        TreeMap<String, BayesNetItem> headers = new TreeMap<>();
        for (KnowledgeBaseHeader header : dataset.getHeaders()) {
            headers.put(header.getTitle(), new BayesNetItem(header));
            cmbAttr.add(header.getTitle());
        }
        DefaultComboBoxModel model = new DefaultComboBoxModel(cmbAttr.toArray());
        cmbAttributeToPredict.setModel(model);
        cmbClassPrecision.setModel(model);
        listAttr.setModel(model);
        listSamplingAttr.setModel(model);
        cmbQueryAttr.setModel(model);

        setLoading("Building Bayes Network.");
        bn = new BayesNet(dataset, headers);

        updateNetworkGraphBuilder();
        hideLoading("Bayes network instantiated. Please, select the network file.");
    }

    private void loadNetwork(JSONObject network) {
        setLoading("Loading Bayes Network.");

        for (Object item : (JSONArray) network.get("attr")) {
            JSONObject rel = (JSONObject) item;
            JAttribute attr = attributeUI.get((String) rel.get("title"));
            System.out.println(Math.toIntExact((long) rel.get("x")));
            attr.setLocation(Math.toIntExact((long) rel.get("x")), Math.toIntExact((long) rel.get("y")));
        }
        for (Object item : (JSONArray) network.get("con")) {
            JSONObject rel = (JSONObject) item;
            String to = (String) rel.get("to"), from = (String) rel.get("from");
            bn.setAsParent(to, from);

            GraphContainer.addArrow(new LineData(attributeUI.get(from), attributeUI.get(to)));
        }

        setLoading("Training Bayes Network.");
        bn.syncNetwork();
        hideLoading("Bayes network loaded.");
    }


    private void btnLoadNetworkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadNetworkActionPerformed
        JFileChooser fcDataset = new JFileChooser();
        fcDataset.setToolTipText("Select the dataset file.");
        fcDataset.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Dataset file", "csv"));
        new Thread(() -> {
            if (fcDataset.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    loadDataset(fcDataset.getSelectedFile());

                    JFileChooser fcNetwork = new JFileChooser();
                    fcNetwork.setToolTipText("Select the network file.");
                    fcNetwork.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Network file", "dll"));
                    if (fcNetwork.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                        JSONParser parser = new JSONParser();
                        StringBuilder network = new StringBuilder();
                        BufferedReader br = new BufferedReader(new FileReader(fcNetwork.getSelectedFile()));
                        String line;
                        while ((line = br.readLine()) != null) {
                            network.append(line);
                        }
                        loadNetwork((JSONObject) parser.parse(network.toString()));
                        br.close();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Something went wrong while loading this dataset.");
                    ex.printStackTrace();
                    hideLoading("Something went wrong while loading the dataset.");
                }
            }
        }).start();

    }//GEN-LAST:event_btnLoadNetworkActionPerformed

    private void btnTestMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTestMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTestMouseClicked

    private void btnTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestActionPerformed
        if (bn == null) {
            JOptionPane.showMessageDialog(this, "You must first instantiate the Bayes Network.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            new Thread(() -> {
                setLoading("Loading test dataset.");
                String className = (String) cmbClassPrecision.getSelectedItem();
                int rights = 0, wrongs = 0;
                try {
                    KnowledgeBase<String> datasetTest = KnowledgeBase.builder()
                            .fromFile(fc.getSelectedFile())
                            .savingHeader(true)
                            .withExampleNameIn(-2)
                            .withClassNameIn(bn.getAttribute(className).getIndex())
                            .withSeparator(",")
                            .build();

                    setLoading("Testing entries.");
                    for (KnowledgeBaseItem<String> item : datasetTest.getItems()) {
                        Predictor.Builder p = Predictor.predict(bn.getAttribute((String) cmbClassPrecision.getSelectedItem()));
                        for (KnowledgeBaseHeader header : datasetTest.getHeaders()) {
                            if (!header.getTitle().equals(className)) {
                                System.out.println(header.getTitle() + " = " + bn.getAttribute(header.getTitle()));
                                p.givenThat(bn.getAttribute(header.getTitle()), item.getItem(header.getIndex()));
                            }
                        }
                        if (bn.runPrediction(p.build()).equals(item.getClassName())) {
                            rights++;
                        } else {
                            wrongs++;
                        }
                    }

                    txtCorrects.setText(rights + "");
                    txtWrong.setText(wrongs + "");
                    txtPrecision.setText((rights / (double) datasetTest.getItems().size()) + "");
                    hideLoading("Testing is done.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    hideLoading("Testing was not executed correctly because of \"" + ex.getLocalizedMessage() + "\".");
                }
            }).start();
        }
    }//GEN-LAST:event_btnTestActionPerformed

    private void btnPredictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPredictActionPerformed
        if (bn == null) {
            JOptionPane.showMessageDialog(this, "You must first instantiate the Bayes Network.");
            return;
        }

        TreeMap<String, Double> p = bn.predict(predicting);

        double value = Double.MIN_VALUE;
        String classAnswer = "";
        StringBuilder sb = new StringBuilder("P (");
        sb.append(predicting.getTarget()).append(" | restrained variables): ");
        for (String className : p.keySet()) {
            if (value < p.get(className)) {
                value = p.get(className);
                classAnswer = className;
            }
            sb.append(classAnswer).append(" = ").append(p.get(className)).append("\r\n");
        }
        sb.append("\r\nResult: ").append(classAnswer);
        JOptionPane.showMessageDialog(this, sb.toString());
        txtPredictedClass.setText(classAnswer);
    }//GEN-LAST:event_btnPredictActionPerformed

    private void cmbAttributeToPredictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAttributeToPredictActionPerformed
        String item = (String) cmbAttributeToPredict.getSelectedItem();
        if (predicting != null) {
            predicting.setTarget(attributeUI.get(item).getAttribute());

            System.out.println("Target is " + predicting.getTarget());
        }
    }//GEN-LAST:event_cmbAttributeToPredictActionPerformed

    private void btnSamplingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSamplingActionPerformed
        int n = (int) txtSamplingN.getValue();
        if (n == 0) {
            JOptionPane.showMessageDialog(this, "Please select the sampling size first.");
            return;
        }

        new Thread(() -> {
            setLoading("Executing sample for " + n + " examples.");
            // In a new thread, we must count the frequence of the current
            // attribute given the restrained variables:
            HashMap<String, Double> counter = new HashMap<>();
            double sum = 0;
            Sampling sampling = null;

            // We start by generating the sampling:
            String sampleAlgorithm = ((String) cmbSamplingAlgorithm.getSelectedItem());
            if (sampleAlgorithm.equals("Prior Sampling")) {
                sampling = new PriorSampling(bn);
            } else if (sampleAlgorithm.equals("Rejection Sampling")) {
                sampling = new RejectionSampling(bn);
            } else {
                sampling = new LikelihoodWeightSampling(bn);
            }
            setLoading("Generating sample. This may take awhile.");
            String selectedAttribute = (String) cmbQueryAttr.getSelectedItem();
            int selectedAttrIndex = attributeUI.get(selectedAttribute).getAttribute().getIndex();

            // We will go throught all sampled values to calculate 
            // the frequence for this possible value:
            for (Object sampleItem : sampling.getSampling(samplingValues, n)) {
                if (sampleItem == null) {
                    System.out.println("Skiping because sample is null.");
                    continue;
                }

                SampleItem<String> s = (SampleItem) sampleItem;
                KnowledgeBaseItem<String> item = s.getKey();

                boolean allMatches = true;
                for (String attrName : samplingValues.keySet()) {
                    if (!samplingValues.get(attrName).equals(item.getItem(attributeUI.get(attrName).getAttribute().getIndex()))) {
                        // This sample doesn't match all constrainst.
                        allMatches = false;
                        break;
                    }
                }

                if (allMatches) {
                    // This sample matches all constraints.
                    counter.put(s.getKey().getItem(selectedAttrIndex), counter.getOrDefault(item.getItem(selectedAttrIndex), 0.0) + s.getValue());
                    sum += s.getValue();
                }
            }

            setLoading("Sampling done. Calculating frequence of the given query.");

            for (String possibleValue : attributeUI.get(selectedAttribute).getAttribute().getPossibleValues()) {
                counter.put(possibleValue, counter.getOrDefault(possibleValue, 0.0) / sum);
            }

            // Show the results:
            StringBuilder sb = new StringBuilder();
            sb.append("P (").append(cmbQueryAttr.getSelectedItem())
                    .append(" | ")
                    .append("restrained variables")
                    .append(") for ")
                    .append(n)
                    .append(" samples.\r\n");

            for (String possibleValue : attributeUI.get((String) cmbQueryAttr.getSelectedItem()).getAttribute().getPossibleValues()) {
                sb.append(cmbQueryAttr.getSelectedItem())
                        .append(" = ")
                        .append(possibleValue)
                        .append(": \t")
                        .append(counter.get(possibleValue).toString().replace(".", ","))
                        .append("\r\n");
            }
            txtSamplingResults.setText(sb.toString());
            hideLoading("Sampling is done.");
            JOptionPane.showMessageDialog(this, sb.toString());
        }).start();

    }//GEN-LAST:event_btnSamplingActionPerformed

    private void btnSaveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSaveMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        JSONObject json = new JSONObject();
        JSONArray attributes = new JSONArray();
        JSONArray connections = new JSONArray();
        for (JAttribute i : attributeUI.values()) {
            JSONObject attr = new JSONObject();
            attr.put("title", i.getTitle());
            attr.put("x", i.getX());
            attr.put("y", i.getY());
            attributes.add(attr);
        }

        for (LineData con : GraphContainer.getLines()) {
            JSONObject line = new JSONObject();
            line.put("from", con.getFrom().getTitle());
            line.put("to", con.getTo().getTitle());
            connections.add(line);
        }

        json.put("attr", attributes);
        json.put("con", connections);
        json.toJSONString();

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Network file", "dll"));
        if (fc.showSaveDialog(fc) == JFileChooser.APPROVE_OPTION) {
            try {
                String file = fc.getSelectedFile().getAbsolutePath();
                if (!file.endsWith(".dll")) {
                    file += ".dll";
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                bw.write(json.toJSONString());
                bw.close();
                JOptionPane.showMessageDialog(fc, "Network was saved successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(fc, "Something went wrong while saving this network.");
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    /**
     * This function is responsable for updating the graphical representation of
     * the network as well as adding all listeners for the interactions.
     */
    private void updateNetworkGraphBuilder() {
        if (bn == null) {
            return;
        }

        // GraphContainer must not have a layout, we will deal with all
        // the rendering and positioning ourselfs.
        GraphContainer.setLayout(null);
        attributeUI = new HashMap<>();
        GraphContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent me) {

                if (cLine != null) {
                    // The user was dragging a arrow to conect to a child
                    // and clicked outside. We must cancel this action:
                    GraphContainer.removeArrow(cLine);
                    cLine = null;
                    cParentAttribute = null;
                    GraphContainer.repaint();
                }
            }
        });

        GraphContainer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                // If the user is currently dragging an arrow, we must
                // redraw the panel each pixel.

                // This should be handled by the Graph Container. But
                // there is no point refactoring now.
                if (cLine != null) {
                    GraphContainer.repaint();
                }
            }
        });

        // We are ready to load the dataset into the graph:
        int x = 10, y = 10;
        for (String attrTitle : bn.getAttributes().keySet()) {
            JAttribute attr = new JAttribute(bn.getAttribute(attrTitle), x, y);
            x += 170;
            if ((x % (170 * 6)) == 10) {
                x = 10;
                y += 170;
            }

            attr.setParentEvents((JAttribute jAttribute) -> {
                // This function is called when the user first clicks the "OUT"
                // element of an attribute. We must set up the dragging]
                // arrow functionality
                if (cLine != null) {
                    GraphContainer.removeArrow(cLine);
                    cLine = null;
                }
                cParentAttribute = jAttribute;
                cLine = new LineData(GraphContainer, jAttribute);
                GraphContainer.addArrow(cLine);
                GraphContainer.repaint();
            });

            attr.setChildEvents(new JAttribute.OnSetAsChild() {
                @Override
                public void showParentRemover(HashSet<BayesNetItem> parents, BayesNetItem item) {
                    // We must now show the remove parent menu:
                    parentRemover.removeAll();

                    if (parents == null) {
                        return;
                    }

                    for (BayesNetItem parent : parents) {
                        // We must create a new menu item for each parent:
                        JMenuItem menuItem = new JMenuItem(parent.getTitle());
                        try {
                            menuItem.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("Resources/remove.png"))));
                        } catch (IOException ignored) {
                        }
                        menuItem.addActionListener((ActionEvent ae) -> {
                            item.removeParent(parent);
                            GraphContainer.removeArrow(attributeUI.get(parent.getTitle()), attr);
                            GraphContainer.repaint();
                            bn.syncNetwork();
                        });
                        parentRemover.add(menuItem);
                    }

                    // Finally, we show the component:
                    parentRemover.show(MainApp.this, getMousePosition().x, getMousePosition().y);
                }

                @Override
                public boolean OnSetAsChild(BayesNetItem item) {
                    if (cParentAttribute != null) {
                        // The user select this attribute to be used as the child
                        // in the current interaction.

                        // We first update the Bayes Network:
                        bn.setAsParent(item.getTitle(), cParentAttribute.getTitle());
                        bn.syncNetwork();

                        // We now update the graphical representation:
                        GraphContainer.addArrow(new LineData(cParentAttribute, attributeUI.get(item.getTitle())));
                        GraphContainer.removeArrow(cLine);

                        // We clear the unecessary resources and redraw the graphical
                        // interface one last time to show the last arrow:
                        cParentAttribute = null;
                        cLine = null;
                        GraphContainer.repaint();

                        return true; // A parent was set.
                    }
                    return false; // No parent was set.
                }
            });

            attributeUI.put(attrTitle, attr);
            GraphContainer.add(attr);
            generateAttributeMouseController(attr);
        }

        GraphContainer.revalidate();
        GraphContainer.repaint();
    }

    /**
     * Generates all the motion events to a component.
     *
     * @param attr The current attribute.
     */
    private void generateAttributeMouseController(JAttribute attr) {
        final Point cPoint = new Point();
        attr.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                // We are setting up the dragging function:
                cPoint.setLocation(me.getLocationOnScreen());
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                // We are updating the JAttribution to the current location:
                cPoint.setLocation(me.getLocationOnScreen());

                GraphContainer.revalidate();
                GraphContainer.repaint();
            }
        });
        attr.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                // We are dragging the JAttribute to a new location:
                attr.setLocation(attr.getLocation().x + (me.getXOnScreen() - cPoint.x), attr.getLocation().y + (me.getYOnScreen() - cPoint.y));
                cPoint.setLocation(me.getLocationOnScreen());
                GraphContainer.repaint();
            }
        });
    }

    /**
     * Sets the status texts and shows the loading icon.
     *
     * @param msg Message to show.
     */
    private void setLoading(String msg) {
        loading.setVisible(true);
        txtStatus.setText(msg);
    }

    /**
     * Hide the loading icon.
     */
    private void hideLoading(String msg) {
        loading.setVisible(false);
        txtStatus.setText(msg);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());

        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainApp.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.Components.GraphContainer GraphContainer;
    private javax.swing.JScrollPane GraphContainerScroll;
    private javax.swing.JButton btnLoad;
    private javax.swing.JMenuItem btnLoadDataset;
    private javax.swing.JMenuItem btnLoadNetwork;
    private javax.swing.JButton btnPredict;
    private javax.swing.JButton btnSampling;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnTest;
    private javax.swing.JComboBox<String> cmbAttributeToPredict;
    private javax.swing.JComboBox<String> cmbClassPrecision;
    private javax.swing.JComboBox<String> cmbQueryAttr;
    private javax.swing.JComboBox<String> cmbSamplingAlgorithm;
    private javax.swing.JComboBox<String> cmbSamplingValue;
    private javax.swing.JComboBox<String> cmbValue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JList<String> listAttr;
    private javax.swing.JList<String> listSamplingAttr;
    private javax.swing.JLabel loading;
    private javax.swing.JPopupMenu menuLoad;
    private javax.swing.JPopupMenu parentRemover;
    private javax.swing.JPanel predictingValuePanel;
    private javax.swing.JPanel samplingValuePanel;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JTextField txtCorrects;
    private javax.swing.JLabel txtPrecision;
    private javax.swing.JTextField txtPredictedClass;
    private javax.swing.JSpinner txtSamplingN;
    private javax.swing.JTextArea txtSamplingResults;
    private javax.swing.JLabel txtStatus;
    private javax.swing.JTextField txtWrong;
    // End of variables declaration//GEN-END:variables
}
