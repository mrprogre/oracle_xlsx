package QueriesCreater;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

public class Gui extends JFrame {
    static JTable columnsTable;
    static DefaultTableModel model;
    static JTable paramsTable;
    ImageIcon logo = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Gui.class.getResource("/logo.png")));
    ImageIcon colors = new ImageIcon(Toolkit.getDefaultToolkit().createImage(Gui.class.getResource("/colors.png")));
    DefaultTableModel objectModel;
    JComboBox<String> typesCombobox = new JComboBox<>();
    JComboBox<String> booleanCombobox = new JComboBox<>();
    JComboBox<String> hAlignCombobox = new JComboBox<>();
    JComboBox<String> vAlignCombobox = new JComboBox<>();
    static JComboBox<String> fontsCombobox = new JComboBox<>();
    final String [] plsqlTypes = {"NUMBER","VARCHAR2(4000)","DATE","CHAR(69)","NCHAR","NVARCHAR2","LONG","RAW","LONG_RAW",
            "NUMERIC","DEC","DECIMAL","PLS_INTEGER","BFILE","BLOB","CLOB","NCLOB",
            "BOOLEAN","ROWID"};

    public Gui() {
        this.setResizable(false);
        setIconImage(logo.getImage());
        this.setTitle("PL/SQL: excel export");
        this.setFont(new Font("Tahoma", Font.PLAIN, 14));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(650, 190, 560, 582);
        this.getContentPane().setBackground(new Color(233, 150, 122));
        this.getContentPane().setLayout(null);

        // типы данных PL/SQL
        for (String type : plsqlTypes) {
            typesCombobox.addItem(type);
        }
        // шрифты
        Fonts.fontsList();
        // boolean
        booleanCombobox.addItem("true");
        booleanCombobox.addItem("false");
        // alignments
        hAlignCombobox.addItem("left");
        hAlignCombobox.addItem("right");
        hAlignCombobox.addItem("center");
        vAlignCombobox.addItem("top");
        vAlignCombobox.addItem("bottom");
        vAlignCombobox.addItem("center");

        // Object name table
        final JScrollPane objectNames = new JScrollPane();
        objectNames.setBounds(10, 25, 380, 210);
        getContentPane().add(objectNames);

        String[] objectColumns = new String[]{"Object", "Value"};
        objectModel = new DefaultTableModel(new Object[][] {
                {"Function name", "get_xls_function"},
                {"Procedure name", "get_xls_from_table"},
                {"Record type", "t_type_of_record"},
                {"Table as record", "t_type_of_record_tbl"},
                {"Table or view", "table_or_view"},
                {"Headers background", "#FFCC66"},
                {"Headers font size", 13},
                {"Rows font size", 12},
                {"Rows height", 25},
                {"Rows bold", false},
                {"Rows italic", false},
                {"Headers font", "Times New Roman"},
                {"Rows font", "Times New Roman"},
                {"Horizontal alignment", "center"},
                {"Vertical alignment", "center"},
                {"Wrap text", true},
        },objectColumns) {
            final boolean[] columnEditables = new boolean[]{
                    false, true
            };
            public boolean isCellEditable(int row, int column) {
                return this.columnEditables[column];
            }
        };
        paramsTable = new JTable(objectModel){
            // combo's
            public TableCellEditor getCellEditor(int row, int column){
                int modelColumn = convertColumnIndexToModel(column);

                if (modelColumn == 1 && (row == 11 || row == 12)){
                    return new DefaultCellEditor(fontsCombobox);
                } else if (modelColumn == 1 && (row == 9 || row == 10)|| row == 15) {
                    return new DefaultCellEditor(booleanCombobox);
                } else if (modelColumn == 1 && row == 13) {
                    return new DefaultCellEditor(hAlignCombobox);
                } else if (modelColumn == 1 && row == 14) {
                    return new DefaultCellEditor(vAlignCombobox);
                } else
                    return super.getCellEditor(row, column);
            }
        };
        paramsTable.setDefaultRenderer(Object.class, new TableInfoRenderer());
        // cell border color
        paramsTable.setGridColor(new Color(58, 79, 79));
        paramsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        paramsTable.setFillsViewportHeight(true);
        paramsTable.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader objectHeader = paramsTable.getTableHeader();
        objectHeader.setFont(new Font("Tahoma", Font.BOLD, 13));
        //cell alignment
        TableInfoRenderer objectRenderer = new TableInfoRenderer();
        objectRenderer.setHorizontalAlignment(JLabel.LEADING);
        paramsTable.getColumnModel().getColumn(0).setCellRenderer(objectRenderer);
        paramsTable.getColumnModel().getColumn(1).setCellRenderer(objectRenderer);
        paramsTable.setRowHeight(24);
        paramsTable.setColumnSelectionAllowed(true);
        paramsTable.setCellSelectionEnabled(true);
        paramsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paramsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        paramsTable.getColumnModel().getColumn(0).setPreferredWidth(158);
        paramsTable.getColumnModel().getColumn(1).setPreferredWidth(204);
        //colors
        paramsTable.setSelectionBackground(new Color(254, 204, 204));
        objectNames.setViewportView(paramsTable);

        // удаление содержимого ячейки кнопкой Delete
        paramsTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==127){
                    paramsTable.setValueAt("", paramsTable.getSelectedRow(), paramsTable.getSelectedColumn());
                }
            }
        });

        // Columns table
        final JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 255, 520, 282);
        this.getContentPane().add(scrollPane);
        String[] columns = new String[]{"Header", "Width", "DB Column", "DB Type"};
        model = new DefaultTableModel(new Object[69][], columns) {
            final boolean[] columnEditables = new boolean[]{
                    true, true, true, true
            };
            public boolean isCellEditable(int row, int column) {
                return this.columnEditables[column];
            }
        };
        columnsTable = new JTable(model);

        // cell border color
        columnsTable.setGridColor(new Color(58, 79, 79));
        columnsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // table background color
        columnsTable.setFillsViewportHeight(true);
        columnsTable.setBackground(new Color(250, 252, 255));
        // headers settings
        JTableHeader header = columnsTable.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 13));
        //cell alignment
        TableColumnsRenderer Renderer = new TableColumnsRenderer();
        Renderer.setHorizontalAlignment(JLabel.CENTER);
        columnsTable.getColumnModel().getColumn(0).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(1).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(2).setCellRenderer(Renderer);
        columnsTable.getColumnModel().getColumn(3).setCellRenderer(Renderer);
        // Типы данных - комбобокс
        TableColumn testColumn = columnsTable.getColumnModel().getColumn(3);
        testColumn.setCellEditor(new comboBoxRenderer(typesCombobox));
        columnsTable.setRowHeight(24);
        columnsTable.setColumnSelectionAllowed(true);
        columnsTable.setCellSelectionEnabled(true);
        columnsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        columnsTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        columnsTable.getColumnModel().getColumn(0).setPreferredWidth(158);
        columnsTable.getColumnModel().getColumn(1).setPreferredWidth(58);
        columnsTable.getColumnModel().getColumn(2).setPreferredWidth(164);
        columnsTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        //colors
        columnsTable.setSelectionBackground(new Color(254, 204, 204));
        scrollPane.setViewportView(columnsTable);

        // удаление содержимого ячейки кнопкой Delete
        columnsTable.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 127){
                    columnsTable.setValueAt(null, columnsTable.getSelectedRow(), columnsTable.getSelectedColumn());
                }
            }
        });

        // Open CSV file
        JButton openCsvFile = new JButton("Open file");
        openCsvFile.setBounds(410, 25, 120, 22);
        openCsvFile.setBackground(new Color(203, 221, 251));
        openCsvFile.setFont(new Font("Tahoma", Font.BOLD, 11));
        openCsvFile.setContentAreaFilled(true);
        openCsvFile.setBorderPainted(true);
        openCsvFile.setFocusable(false);
        getContentPane().add(openCsvFile);
        openCsvFile.addActionListener(e -> openFile());

        // Create objects button
        JButton setColumnsBtn = new JButton("Create objects");
        setColumnsBtn.setBounds(410, 58, 120, 22);
        setColumnsBtn.setBackground(new Color(192, 225, 255));
        setColumnsBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        setColumnsBtn.setContentAreaFilled(true);
        setColumnsBtn.setBorderPainted(true);
        setColumnsBtn.setFocusable(false);
        getContentPane().add(setColumnsBtn);
        setColumnsBtn.addActionListener((e) -> getValues());

        // Clear parameters
        JButton clearParams = new JButton("Clear params");
        clearParams.setFont(new Font("Tahoma", Font.BOLD, 11));
        clearParams.setFocusable(false);
        clearParams.setContentAreaFilled(true);
        clearParams.setBorderPainted(true);
        clearParams.setBackground(new Color(251, 203, 203));
        clearParams.setBounds(410, 91, 120, 22);
        getContentPane().add(clearParams);
        clearParams.addActionListener((e) -> {
            for (int i = 0; i < paramsTable.getRowCount(); i++)
                if (i != 5) paramsTable.setValueAt(null, i, 1);
                else paramsTable.setValueAt("#FFFFFF", i, 1);
        });

        // Clear columns
        JButton clearTableBtn = new JButton("Clear columns");
        clearTableBtn.setBounds(410, 124, 120, 22);
        clearTableBtn.setBackground(new Color(251, 203, 203));
        clearTableBtn.setFont(new Font("Tahoma", Font.BOLD, 11));
        clearTableBtn.setContentAreaFilled(true);
        clearTableBtn.setBorderPainted(true);
        clearTableBtn.setFocusable(false);
        getContentPane().add(clearTableBtn);
        clearTableBtn.addActionListener((e) -> {
            for (int i = 0; i < columnsTable.getRowCount(); i++)
                for(int j = 0; j < columnsTable.getColumnCount(); j++) {
                    columnsTable.setValueAt(null, i, j);
                }
        });

        JLabel objectNameLbl = new JLabel("Parameters");
        objectNameLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        objectNameLbl.setBounds(10, 7, 334, 18);
        getContentPane().add(objectNameLbl);

        JLabel columnLbl = new JLabel("Columns");
        columnLbl.setFont(new Font("Tahoma", Font.BOLD, 13));
        columnLbl.setBounds(10, 237, 334, 18);
        getContentPane().add(columnLbl);

        // Цвет для заголовков
        JButton backGround = new JButton();
        backGround.setIcon(colors);
        backGround.setToolTipText("Choose a background color for headers");
        backGround.setFont(new Font("Tahoma", Font.BOLD, 11));
        backGround.setFocusable(false);
        backGround.setContentAreaFilled(false);
        backGround.setBorderPainted(true);
        backGround.setBackground(new Color(232, 37, 137));
        backGround.setBounds(395, 166, 26, 23);
        getContentPane().add(backGround);
        backGround.addActionListener(e -> {
            Color color = JColorChooser.showDialog(null, "Color", Color.black);
            if (color != null) paramsTable.setValueAt(toHexString(color).toUpperCase(), 5, 1);
        });

        this.setVisible(true);
    }

    // Получение данных из таблиц
    public static void getValues() {
        // определяем количество строк для создания массива (количество столбцов статическое - 4)
        int rowCount = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (columnsTable.getValueAt(i, 0) != null) {
                rowCount++;
            }
        }

        // параметры
        Object[][] objectParameters = new Object[16][2];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                if (paramsTable.getValueAt(i, j) != null) {
                    objectParameters [i][j] = paramsTable.getValueAt(i, j);
                }
            }
        }

        // столбцы
        Object[][] columns = new Object[rowCount][4];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < 4; j++) {
                if (columnsTable.getValueAt(i, j) != null) {
                    columns [i][j] = columnsTable.getValueAt(i, j);
                }
            }
        }

        // выгрузка запросов в текстовый файл
        if (columns.length > 0) {
            write(new GetSqlQuery().getQueries(objectParameters, columns));
        }
    }

    // Открытие файла для парсинга
    static void openFile(){
        JFileChooser open = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT or CSV", "csv", "txt");
        open.setFileFilter(filter);
        open.setCurrentDirectory(new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
        int ret = open.showDialog(null, "Open");
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = open.getSelectedFile();
            getLinesFromFile(file.toString());
        }
    }

    // Запись кода объектов в файл
    static void write(String pSql) {
        //Save file to
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "*.txt", "*.TXT", "*.*");
        JFileChooser save_to = new JFileChooser();
        save_to.setFileFilter(filter);
        save_to.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
        int ret = save_to.showDialog(null, "Save");
        if (ret == JFileChooser.APPROVE_OPTION) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(save_to.getSelectedFile() + ".txt"), StandardCharsets.UTF_8)) {
                writer.write(pSql.replace("#", ""));
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Подсчет количества строк в файле
    static int countLines(String path) {
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(path));
            int cnt;
            while (true) {
                if (reader.readLine() == null) break;
            }
            cnt = reader.getLineNumber();
            reader.close();
            return cnt;
        } catch (IOException io) {
            io.printStackTrace();
        }
        return 0;
    }

    // Считывание всех строк из файла в двумерный массив строк
    static void getLinesFromFile(String path) {
        int rowsCount = Gui.countLines(path);
        String[][] lines = new String[rowsCount][];
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null && i < rowsCount) {
                lines[i++] = line.split(";");

                for (int j = 0; j < 4; j++) {
                    try {
                        columnsTable.setValueAt(lines[i - 1][j], i - 1, j);
                    } catch (IndexOutOfBoundsException e){
                        columnsTable.setValueAt("-", i - 1, j);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Таблицы с объектами (renderer)
    public static class TableInfoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if(column == 0) {
                if (isSelected) {
                    super.setForeground(new Color(99, 9, 9));
                } else {
                    super.setForeground(Color.BLACK);
                }
                c.setBackground(new Color(219, 234, 201));
                c.setHorizontalAlignment(LEFT);
                c.setFont(new Font("Tahoma", Font.BOLD,13));
            } else if (column == 1 && row == 5){
                Object color = paramsTable.getValueAt(5,1);
                if (color != null) c.setBackground(Color.decode(color.toString()));
            } else {
                c.setBackground(Color.WHITE);
                c.setHorizontalAlignment(LEFT);
                if (isSelected) {
                    super.setBackground(new Color(254, 204, 204));
                }
            }
            return c;
        }
    }

    // Таблицы со столбцами (renderer)
    public static class TableColumnsRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            if(column == 0) {
                if (isSelected) {
                    super.setForeground(new Color(28, 86, 154));
                } else {
                    super.setForeground(Color.BLACK);
                }
                c.setBackground(new Color(201, 225, 234));
                c.setHorizontalAlignment(LEFT);
                c.setFont(new Font("Tahoma", Font.BOLD,13));
            } else if (column == 1) {
                if (isSelected) {
                    super.setForeground(new Color(154, 28, 68));
                    super.setFont(new Font("Tahoma", Font.BOLD,13));
                } else {
                    super.setForeground(Color.BLACK);
                }
                c.setBackground(new Color(255, 255, 255));
                c.setHorizontalAlignment(CENTER);
            } else {
                c.setBackground(Color.WHITE);
                c.setHorizontalAlignment(LEFT);
                if (isSelected) {
                    super.setBackground(new Color(254, 204, 204));
                    super.setFont(new Font("Tahoma", Font.BOLD,13));
                }
            }
            return c;
        }
    }

    // Ячейка с типами (comboBox renderer)
    public static class comboBoxRenderer extends DefaultCellEditor {
        public comboBoxRenderer(JComboBox comboBox) {
            super(comboBox);
            super.setClickCountToStart(1);
            comboBox.setEditable(true);
        }
    }

    static String toHexString(Color c) {
        if (c != null) {
            StringBuilder sb = new StringBuilder("#");
            if (c.getRed() < 16) sb.append('0');
            sb.append(Integer.toHexString(c.getRed()));
            if (c.getGreen() < 16) sb.append('0');
            sb.append(Integer.toHexString(c.getGreen()));
            if (c.getBlue() < 16) sb.append('0');
            sb.append(Integer.toHexString(c.getBlue()));
        return sb.toString();
        }
        return null;
    }
}
