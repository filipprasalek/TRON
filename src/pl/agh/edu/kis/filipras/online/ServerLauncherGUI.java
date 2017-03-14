package pl.agh.edu.kis.filipras.online;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by filipras on 24.01.2017.
 */
public class ServerLauncherGUI extends JPanel{

    private JFrame frame;
    private JComboBox serverCapacity;
    private GridBagConstraints layout;
    private JTextField port;
    private JTextField serverStatus;
    private JButton buttonAdd;
    private boolean isRunning;

    public ServerLauncherGUI(JFrame f){
        layout = new GridBagConstraints();
        setLayout(new GridBagLayout());
        this.frame = f;

        initializeLabels();
        initializeTextBoxes();
        initializeButtons();
    }

    private void initializeLabels(){

        layout.weighty = 1;
        layout.weightx = 1;

        layout.anchor = GridBagConstraints.LINE_END;

        JTextField portLabel = new JTextField("Port: ");
        portLabel.setEditable(false);
        portLabel.setBorder(BorderFactory.createEmptyBorder());
        layout.gridx = 0;
        layout.gridy = 0;
        this.add(portLabel,layout);

        JTextField serverCapacityLabel;
        portLabel.setEditable(false);
        serverCapacityLabel = new JTextField("How many players?");
        serverCapacityLabel.setEditable(false);
        serverCapacityLabel.setBorder(BorderFactory.createEmptyBorder());
        layout.gridx = 0;
        layout.gridy = 1;
        this.add(serverCapacityLabel,layout);

        serverStatus = new JTextField("Server offline ");
        serverStatus.setEditable(false);
        serverStatus.setBorder(BorderFactory.createEmptyBorder());
        layout.gridx = 0;
        layout.gridy = 2;
        this.add(serverStatus,layout);



    }

    private void initializeTextBoxes(){

        layout.anchor = GridBagConstraints.LINE_START;
        port = new JTextField(5);
        port.setColumns(5);
        layout.gridx = 1;
        layout.gridy = 0;
        this.add(port,layout);

    }

    private void initializeButtons(){

        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("2");
        serverCapacity = new JComboBox(model);
        layout.gridx = 1;
        layout.gridy = 1;
        this.add(serverCapacity,layout);


        buttonAdd = new JButton("Launch!");
        layout.weighty = 3;
        layout.gridx = 1;
        layout.gridy = 2;
        this.add(buttonAdd,layout);
        buttonAdd.addActionListener(new AddButtonAction());

    }

    private class AddButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){

            int portNumber;

            ServerLauncher serverLauncher = new ServerLauncher();

            try{
                portNumber = Integer.parseInt(port.getText());
            } catch (NumberFormatException exception1){
                JOptionPane.showMessageDialog(frame,"Please enter correct port");
                return;
            }

            if (port.getText().equals("")){
                JOptionPane.showMessageDialog(frame,"Please enter port");
                return;
            }

            if (serverLauncher.portAvailable(portNumber)) {
                serverLauncher.initServer(portNumber,Integer.parseInt(serverCapacity.getSelectedItem().toString()));
                serverStatus.setText("Server online");
                buttonAdd.setEnabled(false);
                isRunning = true;

            } else {
                JOptionPane.showMessageDialog(frame, "Port unavailable ");
                return;
            }

        }
    }
}
