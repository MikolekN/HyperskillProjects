package editor;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class TextEditor extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    String textToSave2 = "          Sonnet I\n" +
            "\n" +
            "\n" +
            "FROM fairest creatures we desire increase,\n" +
            "That thereby beauty's rose might never die,\n" +
            "But as the riper should by time decease,\n" +
            "His tender heir might bear his memory:\n" +
            "But thou, contracted to thine own bright eyes,\n" +
            "Feed'st thy light'st flame with self-substantial fuel,\n" +
            "Making a famine where abundance lies,\n" +
            "Thyself thy foe, to thy sweet self too cruel.\n" +
            "Thou that art now the world's fresh ornament\n" +
            "And only herald to the gaudy spring,\n" +
            "Within thine own bud buriest thy content\n" +
            "And, tender churl, makest waste in niggarding.\n" +
            "Pity the world, or else this glutton be,\n" +
            "To eat the world's due, by the grave and thee.\n" +
            "\n" +
            " Sonnet II\n" +
            "       \n" +
            "         \n" +
            "When forty winters shall beseige thy brow,\n" +
            "And dig deep trenches in thy beauty's field,\n" +
            "Thy youth's proud livery, so gazed on now,\n" +
            "Will be a tatter'd weed, of small worth held:\n" +
            "Then being ask'd where all thy beauty lies,\n" +
            "Where all the treasure of thy lusty days,\n" +
            "To say, within thine own deep-sunken eyes,\n" +
            "Were an all-eating shame and thriftless praise.\n" +
            "How much more praise deserved thy beauty's use,\n" +
            "If thou couldst answer 'This fair child of mine\n" +
            "Shall sum my count and make my old excuse,'\n" +
            "Proving his beauty by succession thine!\n" +
            "This were to be new made when thou art old,\n" +
            "And see thy blood warm when thou feel'st it cold.\n" +
            "\n" +
            "Sonnet III\n" +
            "\n" +
            "\n" +
            "Look in thy glass, and tell the face thou viewest\n" +
            "Now is the time that face should form another;\n" +
            "Whose fresh repair if now thou not renewest,\n" +
            "Thou dost beguile the world, unbless some mother.\n" +
            "For where is she so fair whose unear'd womb\n" +
            "Disdains the tillage of thy husbandry?\n" +
            "Or who is he so fond will be the tomb\n" +
            "Of his self-love, to stop posterity?\n" +
            "Thou art thy mother's glass, and she in thee\n" +
            "Calls back the lovely April of her prime:\n" +
            "So thou through windows of thine age shall see\n" +
            "Despite of wrinkles this thy golden time.\n" +
            "But if thou live, remember'd not to be,\n" +
            "Die single, and thine image dies with thee.\n" +
            "\n" +
            "Sonnet IV\n" +
            "\n" +
            "\n" +
            "Unthrifty loveliness, why dost thou spend\n" +
            "Upon thyself thy beauty's legacy?\n" +
            "Nature's bequest gives nothing but doth lend,\n" +
            "And being frank she lends to those are free.\n" +
            "Then, beauteous niggard, why dost thou abuse\n" +
            "The bounteous largess given thee to give?\n" +
            "Profitless usurer, why dost thou use\n" +
            "So great a sum of sums, yet canst not live?\n" +
            "For having traffic with thyself alone,\n" +
            "Thou of thyself thy sweet self dost deceive.\n" +
            "Then how, when nature calls thee to be gone,\n" +
            "What acceptable audit canst thou leave?\n" +
            "Thy unused beauty must be tomb'd with thee,\n" +
            "Which, used, lives th' executor to be.";

    public void saveTextToFile(JFileChooser chooseFile, JTextArea textArea){
        if(chooseFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            try {
                Files.writeString(Paths.get(chooseFile.getSelectedFile().getAbsolutePath()), textArea.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadTextFromFile(JFileChooser chooseFile, JTextArea textArea){
        if(chooseFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (Files.exists(Path.of(chooseFile.getSelectedFile().getAbsolutePath()))) {
                try {
                    textArea.setText(Files.readString(Paths.get(chooseFile.getSelectedFile().getAbsolutePath())));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                textArea.setText("");
            }
        }
    }

    public void searchText(JTextArea textArea, JTextField searchTextField, TreeSet<Integer> found){
        found.clear();
        for(int i = 0; i + searchTextField.getText().length() < textArea.getText().length(); i++){
            found.add(textArea.getText().indexOf(searchTextField.getText(), i));
        }
        found.remove(-1);
    }

    public void searchTextRegex(JTextArea textArea, JTextField searchTextField, TreeSet<Integer> found){
        found.clear();
        Matcher m = Pattern.compile(searchTextField.getText()).matcher(textArea.getText());
        while (m.find()){
            found.add(m.start());
        }
        found.remove(-1);
    }

    public void searchTextFunction(JTextArea textArea, JTextField searchTextField, TreeSet<Integer> found, JCheckBox regex){
        if (regex.isSelected()) {
            searchTextRegex(textArea, searchTextField, found);
        } else {
            searchText(textArea, searchTextField, found);
        }
        if (!found.isEmpty()) {
            int index = found.iterator().next();
            System.out.println(found);
            selectCaret(textArea, searchTextField, regex, index);
        }
    }

    public void selectCaret(JTextArea textArea, JTextField searchTextField, JCheckBox regex, int index){
        if (regex.isSelected()) {
            textArea.setCaretPosition(index + 4);
            textArea.select(index, index + 4);
            textArea.grabFocus();
        } else {
            textArea.setCaretPosition(index + searchTextField.getText().length());
            textArea.select(index, index + searchTextField.getText().length());
            textArea.grabFocus();
        }
    }

    public void previousFunction(JTextArea textArea, JTextField searchTextField, TreeSet<Integer> found, JCheckBox regex){
        if(!found.isEmpty()) {
            if(found.size() == 1){
                selectCaret(textArea, searchTextField, regex, found.iterator().next());
            }
            else if(found.size() == 2){
                System.out.println("Hallo");
                if (regex.isSelected()) {
                    System.out.println("Hallo2");
                    if (found.iterator().next() == textArea.getCaretPosition() - 4) {
                        System.out.println("Hallo3");
                        Iterator<Integer> it = found.iterator();
                        it.next();
                        selectCaret(textArea, searchTextField, regex, it.next());
                    } else {
                        System.out.println("Hallo4");
                        selectCaret(textArea, searchTextField, regex, found.iterator().next());
                    }
                }
                else {
                    if (found.iterator().next() == textArea.getCaretPosition() - searchTextField.getText().length()) {
                        Iterator<Integer> it = found.iterator();
                        it.next();
                        selectCaret(textArea, searchTextField, regex, it.next());
                    } else {
                        selectCaret(textArea, searchTextField, regex, found.iterator().next());
                    }
                }
            }
            else{
                int min = 0, temp, max = 0;
                Iterator<Integer> it = found.iterator();
                if(it.hasNext()){
                    min = it.next();
                    max = it.next();
                }
                while(it.hasNext()){
                    temp = it.next();
                    if(temp < min){
                        min = temp;
                    }
                    if(temp > max){
                        max = temp;
                    }
                }
                if (regex.isSelected()) {
                    if (min == textArea.getCaretPosition() - 4) {
                        selectCaret(textArea, searchTextField, regex, max);
                        return;
                    }
                    int current;
                    int prev = 0;
                    it = found.iterator();
                    while (it.hasNext()) {
                        current = it.next();
                        if (current == textArea.getCaretPosition() - 4) {
                            break;
                        }
                        prev = current;
                    }
                    int index = prev;
                    selectCaret(textArea, searchTextField, regex, index);
                }
                else {
                    if (min == textArea.getCaretPosition() - searchTextField.getText().length()) {
                        selectCaret(textArea, searchTextField, regex, max);
                        return;
                    }
                    int current;
                    int prev = 0;
                    it = found.iterator();
                    while (it.hasNext()) {
                        current = it.next();
                        if (current == textArea.getCaretPosition() - searchTextField.getText().length()) {
                            break;
                        }
                        prev = current;
                    }
                    int index = prev;
                    selectCaret(textArea, searchTextField, regex, index);
                }
            }
            /*int current;
            int prev = 0;
            Iterator<Integer> it = found.iterator();
            System.out.println(found);
            if(it.hasNext()){
                current = it.next();
                if(current == textArea.getCaretPosition() - searchTextField.getText().length()){
                    while(it.hasNext()){
                        current = it.next();
                    }
                    System.out.println(current);
                    selectCaret(textArea, searchTextField, regex, current);
                    return;
                }
            }
            System.out.println("HERE");
            it = found.iterator();
            while (it.hasNext()){
                current = it.next();
                if(current == textArea.getCaretPosition() - searchTextField.getText().length()){
                    break;
                }
                prev = current;
            }
            int index = prev;
            selectCaret(textArea, searchTextField, regex, index);*/
        }
    }

    public void nextFunction(JTextArea textArea, JTextField searchTextField, TreeSet<Integer> found, JCheckBox regex){
        if(!found.isEmpty()) {
            Iterator<Integer> it = found.iterator();
            while (it.hasNext()){
                int current = it.next();
                if(regex.isSelected()) {
                    if (current == textArea.getCaretPosition() - 4) {
                        break;
                    }
                }
                else{
                    if (current == textArea.getCaretPosition() - searchTextField.getText().length()) {
                        break;
                    }
                }
            }
            int index;
            if(it.hasNext()){
                index = it.next();
            }
            else{
                index = found.iterator().next();
            }
            System.out.println(index);
            selectCaret(textArea, searchTextField, regex, index);
        }
    }

    public void createMenu(JTextArea textArea, JFileChooser chooseFile, JTextField searchTextField, TreeSet<Integer> found, JCheckBox regex){
        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("MenuFile");
        JMenuItem openFileMenu = new JMenuItem("Open");
        openFileMenu.setName("MenuOpen");
        openFileMenu.addActionListener(actionEvent -> {
            loadTextFromFile(chooseFile, textArea);
        });
        JMenuItem saveFileMenu = new JMenuItem("Save");
        saveFileMenu.setName("MenuSave");
        saveFileMenu.addActionListener(actionEvent -> {
            saveTextToFile(chooseFile, textArea);
        });
        JMenuItem exitMenu = new JMenuItem("Exit");
        exitMenu.setName("MenuExit");
        exitMenu.addActionListener(actionEvent -> {
            exit(0);
        });
        fileMenu.add(openFileMenu);
        fileMenu.add(saveFileMenu);
        fileMenu.add(exitMenu);

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setName("MenuSearch");
        JMenuItem startSearchMenu = new JMenuItem("Start search");
        startSearchMenu.setName("MenuStartSearch");
        startSearchMenu.addActionListener(actionEvent-> {
            searchTextFunction(textArea, searchTextField, found, regex);
        });

        JMenuItem previousSearchMenu = new JMenuItem("Previous search");
        previousSearchMenu.setName("MenuPreviousMatch");
        previousSearchMenu.addActionListener(actionEvent -> {
            previousFunction(textArea, searchTextField, found, regex);
        });

        JMenuItem nextMatchMenu = new JMenuItem("Next match");
        nextMatchMenu.setName("MenuNextMatch");
        nextMatchMenu.addActionListener(actionEvent -> {
            nextFunction(textArea, searchTextField, found, regex);
        });

        JMenuItem useRegularExpressionsMenu = new JMenuItem("Use regular expressions");
        useRegularExpressionsMenu.setName("MenuUseRegExp");
        useRegularExpressionsMenu.addActionListener(actionEvent -> {
            regex.setSelected(!regex.isSelected());
        });

        searchMenu.add(startSearchMenu);
        searchMenu.add(previousSearchMenu);
        searchMenu.add(nextMatchMenu);
        searchMenu.add(useRegularExpressionsMenu);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        setJMenuBar(menuBar);
    }

    public void createToolbar(JTextArea textArea, JFileChooser chooseFile, JTextField searchTextField, TreeSet<Integer> found, JCheckBox regex){
        JButton open = new JButton(new ImageIcon("C:\\Users\\Mikolaj\\Desktop\\Text Editor\\Text Editor\\task\\src\\resources\\open.png"));
        open.setName("OpenButton");
        open.setPreferredSize(new Dimension(40,40));

        JButton save = new JButton(new ImageIcon("C:\\Users\\Mikolaj\\Desktop\\Text Editor\\Text Editor\\task\\src\\resources\\save.png"));
        save.setName("SaveButton");
        save.setPreferredSize(new Dimension(40,40));

        searchTextField.setName("SearchField");
        searchTextField.setPreferredSize(new Dimension(200,40));

        JButton search = new JButton(new ImageIcon("C:\\Users\\Mikolaj\\Desktop\\Text Editor\\Text Editor\\task\\src\\resources\\search.png"));
        search.setName("StartSearchButton");
        search.setPreferredSize(new Dimension(40,40));

        JButton previous = new JButton(new ImageIcon("C:\\Users\\Mikolaj\\Desktop\\Text Editor\\Text Editor\\task\\src\\resources\\left.png"));
        previous.setName("PreviousMatchButton");
        previous.setPreferredSize(new Dimension(40,40));

        JButton next = new JButton(new ImageIcon("C:\\Users\\Mikolaj\\Desktop\\Text Editor\\Text Editor\\task\\src\\resources\\right.png"));
        next.setName("NextMatchButton");
        next.setPreferredSize(new Dimension(40,40));

        regex.setText("Use regex");
        regex.setName("UseRegExCheckbox");
        regex.setPreferredSize(new Dimension(100, 20));

        open.addActionListener(actionEvent -> {
            loadTextFromFile(chooseFile, textArea);
        });

        save.addActionListener(actionEvent -> {
            saveTextToFile(chooseFile, textArea);
        });

        search.addActionListener(actionEvent-> {
            searchTextFunction(textArea, searchTextField, found, regex);
        });

        previous.addActionListener(actionEvent -> {
            previousFunction(textArea, searchTextField, found, regex);
        });

        next.addActionListener(actionEvent -> {
            nextFunction(textArea, searchTextField, found, regex);
        });

        JPanel menu = new JPanel(new FlowLayout());
        menu.add(open);
        menu.add(save);
        menu.add(searchTextField);
        menu.add(search);
        menu.add(previous);
        menu.add(next);
        menu.add(regex);
        this.add(menu, BorderLayout.NORTH);
    }

    public JTextArea createTextArea(){
        JTextArea textArea = new JTextArea();
        textArea.setName("TextArea");
        //textArea.setText(textToSave2);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        this.add(scrollPane, BorderLayout.CENTER);
        return textArea;
    }
    public TextEditor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setTitle("Text Editor");

        JFileChooser chooseFile = new JFileChooser();
        chooseFile.setName("FileChooser");
        this.add(chooseFile);

        TreeSet<Integer> found = new TreeSet<>(){};
        JTextField searchTextField = new JTextField();
        JCheckBox regex = new JCheckBox();

        JTextArea textArea = createTextArea();
        createToolbar(textArea, chooseFile, searchTextField, found, regex);
        createMenu(textArea, chooseFile, searchTextField, found, regex);

        //SwingUtilities.updateComponentTreeUI(this);
    }
}
