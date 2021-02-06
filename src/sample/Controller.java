package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Controller {
    @FXML
    private TextField ipField;
    @FXML
    private TextArea HostsField;
    @FXML
    private Text Errors_Confirmatio;
    @FXML
    private TextField websiteField;
    @FXML
    private Text ProductiveText;

    //TABLE AND ITS ATRIBUTES
    @FXML
    private TableView<Website> table;

    private TableColumn<Website, String> nameCol = new TableColumn<>("Website");
    private TableColumn<Website, String> ipCol = new TableColumn<>("IP");

    private ObservableList<Website> websitesModels = FXCollections.observableArrayList();
    //END OF TABLE

    String hosts;

    public static boolean ProductiveMode = false;

    public Controller() throws IOException {
    }

    public void addWebsite() {
        if (ProductiveMode) {
            display("Stop productive mode before you add new website!", false);
        } else {
            String ip;
            if (ipField.getText().equals("") || !validIP(ipField.getText())) {
                ip = "127.0.0.1";
            } else ip = ipField.getText();

            if (websiteField.getText().equals("")) {
                display("No website added!", false);
            } else {
                websitesModels.add(new Website(websiteField.getText(), ip));
                table.setItems(websitesModels);
                display("Added to database!", true);
            }
        }
    }

    //I             NITIALIZING
    public void initialize() throws IOException {
        updateHosts();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ip"));
        table.setItems(websitesModels);
        table.getColumns().addAll(nameCol, ipCol);
    }

    //       Removes from table
    public void btnRemoveSelectedOnAction() {
        try {
            websitesModels.remove(websitesModels.indexOf(table.getSelectionModel().getSelectedItem()));
            display("Done", true);
        } catch (Exception e) {
            display("Nothing selected!", false);
        }
    }

    // Displays a message, true== green, false == red==bad
    public void display(String text, boolean positive) {
        Errors_Confirmatio.setFill(positive ? Color.GREEN : Color.RED);
        Errors_Confirmatio.setText(text);
    }

    public void updateHosts() {
        HostsField.setText("");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("C:\\Users\\User\\IdeaProjects\\WebBlocker\\src\\hosts.txt"));
            String line = reader.readLine();
            while (line != null) {
                HostsField.appendText(line);
                HostsField.appendText("\n");
                line = reader.readLine();
                //System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            display("opsie", false);
        }
    }


    public void setDefault() throws IOException {
        if (ProductiveMode) {
            display("Stop productive mode before you can set it back to default!", false);
        } else {
            if (JOptionPane.showConfirmDialog(null,
                    "Do you want to proceed?", "Revert to default file?", JOptionPane.YES_NO_OPTION) == 0)
                HostsField.setText(Def);
            writeFile(Def);
            display("Default set!",true);
        }
        updateHosts();
    }

    public void setCustom() throws IOException {
        if (ProductiveMode)
            display("Stop productive mode before you can Save it!", false);
        else {
            if (JOptionPane.showConfirmDialog(null,
                    "Do you want to proceed?", "Save file?", JOptionPane.YES_NO_OPTION) == 0)
                System.out.println("wrote file");
            writeFile(HostsField.getText());
            display("Custom file saved!",true);
        }
        updateHosts();
    }

    static String  backup;

    public void setProductiveMode() throws IOException {
        if (ProductiveMode)
            display("Its already running!", false);
        else {

            ProductiveMode = true;
            ProductiveText.setFill(Color.GREEN);
            updateProductiveString();
            if (JOptionPane.showConfirmDialog(null,
                    "Do you want to proceed?", "Set productive mode?", JOptionPane.YES_NO_OPTION) == 0) {
                updateHosts();
                backup = HostsField.getText();
                writeFile(HostsField.getText() + "\n" + productiveStr);
            }
        }
        updateHosts();
    }

    public void removeProductiveMode() throws IOException {
        if (JOptionPane.showConfirmDialog(null,
                "Do you want to proceed?", "Disable productive mode?", JOptionPane.YES_NO_OPTION) == 0) {
            display("Productive Mode disabled!",true);
            ProductiveMode=false;
            ProductiveText.setFill(Color.RED);
            writeFile(backup);
            updateHosts();
        }
    }

    public  void writeFile(String text) throws IOException {
        FileWriter writer = new FileWriter("C:\\Users\\User\\IdeaProjects\\WebBlocker\\src\\hosts.txt");
        writer.write(text);
        writer.close();
    }

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            if (ip.endsWith(".")) {
                return false;
            }

            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void updateProductiveString() {
        StringBuilder sb1 = new StringBuilder("");
        for (Website web :
                websitesModels) {
            sb1.append(web.ip + "\t" + web.name + "\n");
        }
        productiveStr = sb1.toString();

    }

    private String before = "";
    private String Def =
            "#\t127.0.0.1       localhost\n" +
                    "#\t::1             localhost\n" +
                    "109.94.209.70      fitgirl-repacks.cc              # Fake FitGirl site\n" +
                    "109.94.209.70      fitgirl-repack.com              # Fake FitGirl site\n" +
                    "109.94.209.70      www.fitgirlrepacks.co           # Fake FitGirl site\n" +
                    "109.94.209.70      www.fitgirl-repacks.cc          # Fake FitGirl site\n" +
                    "109.94.209.70      www.fitgirl-repack.com          # Fake FitGirl site\n" +
                    "109.94.209.70      www.fitgirl-repacks.website     # Fake FitGirl site\n" +
                    "109.94.209.70      ww9.fitgirl-repacks.xyz         # Fake FitGirl site\n" +
                    "109.94.209.70      *.fitgirl-repacks.xyz           # Fake FitGirl site\n" +
                    "127.0.0.1      winbet.bg             # Fake FitGirl site\n" +
                    "109.94.209.70      fitgirl-repack.net              # Fake FitGirl site\n" +
                    "109.94.209.70      www.fitgirl-repack.net          # Fake FitGirl site\n" +
                    "109.94.209.70      fitgirlpack.site                # Fake FitGirl site\n" +
                    "\n" +
                    "127.0.0.1\twinbet.bg";

    private String productiveStr = Def + "\n" +
            "127.0.0.1\tfacebook.com\n";
}
