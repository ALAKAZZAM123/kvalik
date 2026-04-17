import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ExcelLikeTableApp extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ExcelLikeTableApp() {
        setTitle("Производительность схемы (Excel-like)");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. Модель и таблица
        model = new DefaultTableModel(
                new Object[]{"Число N", "Время (сек)", "Получившееся число"}, 0) {
            // Делаем все ячейки редактируемыми
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        table = new JTable(model);

        // 2. Валидация для столбца "Время (сек)" (индекс 1)
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean stopCellEditing() {
                JComponent comp = (JComponent) getComponent();
                String value = ((JTextField) comp).getText().trim();
                try {
                    if (value.isEmpty()) {
                        JOptionPane.showMessageDialog(table, "Поле 'Время (секунды)' не может быть пустым!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    int time = Integer.parseInt(value);
                    if (time < 0) {
                        JOptionPane.showMessageDialog(table, "Время не может быть отрицательным!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return super.stopCellEditing();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(table, "Введите целое число для времени!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Панель кнопок
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("Добавить (Ctrl+N)");
        JButton delBtn = new JButton("Удалить (Delete)");
        JButton clearBtn = new JButton("Очистить (Backspace)");

        addBtn.addActionListener(e -> addRow());
        delBtn.addActionListener(e -> deleteRow());
        clearBtn.addActionListener(e -> clearRow());

        buttonPanel.add(addBtn);
        buttonPanel.add(delBtn);
        buttonPanel.add(clearBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // 4. Горячие клавиши для всей таблицы
        InputMap im = table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = table.getActionMap();

        // Ctrl+N - Добавить строку
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "addRow");
        am.put("addRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRow();
            }
        });

        // Delete - Удалить строку
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
        am.put("deleteRow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRow();
            }
        });

        // Backspace - Очистить содержимое выделенных ячеек
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "clearCell");
        am.put("clearCell", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] rows = table.getSelectedRows();
                if (rows.length > 0) {
                    for (int row : rows) {
                        for(int i = 0; i < 3; i++) {
                            model.setValueAt("", row, i);
                        }
                    }
                }
            }
        });
    }

    private void addRow() {
        model.addRow(new Object[]{"", "", ""});
    }

    private void deleteRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку для удаления.", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            for (int i = 0; i < model.getColumnCount(); i++) {
                model.setValueAt("", selectedRow, i);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Выберите строку для очистки.", "Информация", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExcelLikeTableApp().setVisible(true));
    }
}