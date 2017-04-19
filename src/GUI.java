
import com.sun.prism.paint.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.TimerTask;
import javax.swing.Timer;

/*


 */

/**
 *
 * @author Sokol
 */
public class GUI extends javax.swing.JFrame {

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        jTextArea2.setEnabled(false);
        moveToNextState.setEnabled(false);
        Auto.setEnabled(false);
        tapeSymb5.setEditable(false);
        tapeSymb5.setEnabled(true);
        stateHistory.setEditable(false);
        timer = new Timer(500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                    moveOneStep();  
                    if (currState.getText().equals("q3") ) timer.stop();
            }
            
        });
        
    }
    
    
    
    
     public static String [][] tablica = new String[][]{
    {"5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","1,-,p","5,-,p"}, //0  0,1,2,3,4,5,6,7,8,9,-,Epsilon
    {"1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","1,-,p","3,-,-","2,-,l"}, //1  działa pobierz/wpisz/przesuń
    {"3,3,-","3,4,-","3,5,-","3,6,-","3,7,-","3,8,-","3,9,-","4,0,l","4,1,l","4,2,l","3,-,-","3,-,-"}, //2
    {"3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-"}, //3
    {"3,1,-","3,2,-","3,3,-","3,4,-","3,5,-","3,6,-","3,7,-","3,8,-","3,9,-","4,0,l","8,1,l","3,-,-"}, //4
    {"5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","5,-,p","3,-,-","6,-,l"}, //5
    {"7,7,l","7,8,l","7,9,l","3,0,-","3,1,-","3,2,-","3,3,-","3,4,-","3,5,-","3,6,-","7,-,l","7,-,l"}, //6
    {"7,9,l","3,0,-","3,1,-","3,2,-","3,3,-","3,4,-","3,5,-","3,6,-","3,7,-","3,8,-","-,-,-","9,m,p"}, //7
    {"3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-","3,m,-"}, //8
    {"3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,-,-","3,3,-","3,2,-","3,1,-","3,-,-","3,-,-"}, //9
                                
    };
     
     boolean inputValid = false;
     String acceptVocab = "0123456789";
     String[] inputTable;
     int headPosition=0;
     String lastSymbol;
     Timer timer;
     
     private void moveOneStep() {
         wpiszLiczbe(pobierzSymbol(),getCurrState());
            
         przesunGlowice(lastSymbol,getCurrState());
         updateState(lastSymbol,getCurrState()); //tu powinien być wczesniejszy symbol
         updateLabels();
         initCurrNumber();
     }
     
     private void updateState(String currSymbol, String thisCurrState){
         String nextState = makeArrayOfcurrState(tablica[Integer.parseInt(thisCurrState)][determineColumn(currSymbol)])[0];
         currState.setText("q" + nextState);
         stateHistory.setText(stateHistory.getText() + ", q"+ nextState);
     }
     
     private void updateLabels(){
         clearTape();
         if(headPosition-3>0) tapeSymb1.setText(inputTable[headPosition-3]);
         if(headPosition-2>0) tapeSymb2.setText(inputTable[headPosition-2]);
         if(headPosition-1>0) tapeSymb3.setText(inputTable[headPosition-1]);
         if(headPosition>0) tapeSymb4.setText(inputTable[headPosition]);
         if(!inputTable[headPosition+1].equals("E")) tapeSymb5.setText(inputTable[headPosition+1]);
         if (inputTable.length > headPosition+3) tapeSymb6.setText(inputTable[headPosition+2]);
         if (inputTable.length > headPosition+4) tapeSymb7.setText(inputTable[headPosition+3]);
         if (inputTable.length > headPosition+5) tapeSymb8.setText(inputTable[headPosition+4]);
         if (inputTable.length > headPosition+6) tapeSymb9.setText(inputTable[headPosition+5]);
     }
     
     private void clearTape(){
         tapeSymb1.setText("");
         tapeSymb2.setText("");
         tapeSymb3.setText("");
         tapeSymb4.setText("");
         tapeSymb5.setText("");
         tapeSymb6.setText("");
         tapeSymb7.setText("");
         tapeSymb8.setText("");
         tapeSymb9.setText("");
     }
     
     private String pobierzSymbol(){
         return inputTable[headPosition+1];
     }
     
     private void wpiszLiczbe(String currSymbol, String currState){
         lastSymbol = currSymbol;
         String symbolToWrite = makeArrayOfcurrState(tablica[Integer.parseInt(currState)][determineColumn(currSymbol)])[1];
         if(!symbolToWrite.equals("-") ){
             if(symbolToWrite.equals("m")) {
                 inputTable[headPosition+1]="-";
             } else inputTable[headPosition+1]=symbolToWrite;
         }     
     }
     
     private void przesunGlowice(String currSymbol, String currState){
         String wayToGo = makeArrayOfcurrState(tablica[Integer.parseInt(currState)][determineColumn(currSymbol)])[2];
         
         if (wayToGo.equals("l")){
             headPosition-=1;
         } else if (wayToGo.equals("p")){
             headPosition+=1;
         }
     }
     
     private int determineColumn(String currSymbol){
         if (currSymbol.equalsIgnoreCase("-")){
             return 10;
         } else if (currSymbol.equals("E")){
             return 11;
         } else return Integer.parseInt(currSymbol);
     }
     
     private String getCurrState(){
         return currState.getText().substring(1);
     }
     
      private String[] makeArrayOfcurrState(String statesToCheck){
        return statesToCheck.split(","); //returns what state to go, what to input and in which side to go
    }
     
     private void checkIfValidNumber(){
         boolean validInput = true;
         for(int i=0; i < inputNumberText.getText().length(); i++){ //sprawdzamy każdy symbol, czy akceptowalny
             boolean acceptedSymbol=false;
             for (int j=0; j<acceptVocab.length();j++){
                 if(inputNumberText.getText().substring(i, i+1).equals(acceptVocab.substring(j,j+1))) acceptedSymbol=true;
             }
             if(i==0 && inputNumberText.getText().substring(i, i+1).equals("-")) acceptedSymbol = true; //jak na początku minus string też akceptowany
             
             if (acceptedSymbol == false) validInput=acceptedSymbol; //jeżeli zsymbol zgadza się z jednym z acceptVocab jest akceptowany
         }
         if(inputNumberText.getText().substring(0, 1).equals("0") && 
                 inputNumberText.getText().length() > 1) validInput = false; //jak zero na początku i nic więcej nie ok
         if(inputNumberText.getText().substring(0, 1).equals("-") &&
                 inputNumberText.getText().substring(1, 2).equals("0")) validInput = false; //jak zero bezpośrednio po minusie też źle
             
         inputValid=validInput;
         
     }
     
     private void printInputValidity(){
         if(!inputValid) jTextArea2.setText("Input not valid, please choose a valid number");
         if(inputValid) jTextArea2.setText("Input valid"+"\n" + "Click moveToNextState or Auto");
     }
     
     private void makeTableWithInput(){
         String temp="E"+inputNumberText.getText()+"E";
         inputTable= new String[temp.length()];
         for (int i=0; i < temp.length(); i++){
             inputTable[i]=temp.substring(i, i+1);
         }
     }
     
     private void initializeTape(){
         tapeSymb5.setText(inputTable[1]);
         if (inputTable.length > 3) tapeSymb6.setText(inputTable[2]);
         if (inputTable.length > 4) tapeSymb7.setText(inputTable[3]);
         if (inputTable.length > 5) tapeSymb8.setText(inputTable[4]);
         if (inputTable.length > 6) tapeSymb9.setText(inputTable[5]);
     }
     
     private void initCurrNumber(){
         String currentState="";
         for (int i=0; i < inputTable.length;i++){
             if(!inputTable[i].equals("E")){
                currentState+=inputTable[i]; 
             }            
         }
         currNumber.setText(currentState);
     }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        moveToNextState = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        currState = new javax.swing.JLabel();
        Auto = new javax.swing.JButton();
        inputNumberText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        currNumber = new javax.swing.JLabel();
        textPrinter = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        acceptNumber = new javax.swing.JButton();
        tapeSymb1 = new javax.swing.JTextField();
        tapeSymb2 = new javax.swing.JTextField();
        tapeSymb3 = new javax.swing.JTextField();
        tapeSymb4 = new javax.swing.JTextField();
        tapeSymb5 = new javax.swing.JTextField();
        tapeSymb6 = new javax.swing.JTextField();
        tapeSymb7 = new javax.swing.JTextField();
        tapeSymb8 = new javax.swing.JTextField();
        tapeSymb9 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        stateHistory = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        moveToNextState.setText("moveToNextState");
        moveToNextState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveToNextStateActionPerformed(evt);
            }
        });

        jLabel1.setText("currState");

        currState.setText("q0");

        Auto.setText("Auto");
        Auto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AutoActionPerformed(evt);
            }
        });

        inputNumberText.setText("0");

        jLabel3.setText("InputNumber");

        jLabel4.setText("Current state of a tape in neighborhood of the head");

        jLabel5.setText("currNumber");

        currNumber.setText("0");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        textPrinter.setViewportView(jTextArea2);

        acceptNumber.setText("acceptNumber");
        acceptNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptNumberActionPerformed(evt);
            }
        });

        tapeSymb1.setEditable(false);
        tapeSymb1.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb1ActionPerformed(evt);
            }
        });

        tapeSymb2.setEditable(false);
        tapeSymb2.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb2ActionPerformed(evt);
            }
        });

        tapeSymb3.setEditable(false);
        tapeSymb3.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb3ActionPerformed(evt);
            }
        });

        tapeSymb4.setEditable(false);
        tapeSymb4.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb4.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb4ActionPerformed(evt);
            }
        });

        tapeSymb5.setEditable(false);
        tapeSymb5.setBackground(new java.awt.Color(100, 240, 240));
        tapeSymb5.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        tapeSymb5.setOpaque(false);
        tapeSymb5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb5ActionPerformed(evt);
            }
        });

        tapeSymb6.setEditable(false);
        tapeSymb6.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb6.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb6ActionPerformed(evt);
            }
        });

        tapeSymb7.setEditable(false);
        tapeSymb7.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb7.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb7ActionPerformed(evt);
            }
        });

        tapeSymb8.setEditable(false);
        tapeSymb8.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb8.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb8ActionPerformed(evt);
            }
        });

        tapeSymb9.setEditable(false);
        tapeSymb9.setBackground(new java.awt.Color(210, 240, 240));
        tapeSymb9.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        tapeSymb9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tapeSymb9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tapeSymb9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tapeSymb9ActionPerformed(evt);
            }
        });

        jLabel2.setText("stateHistory");

        stateHistory.setColumns(20);
        stateHistory.setRows(5);
        stateHistory.setText("q0");
        jScrollPane1.setViewportView(stateHistory);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(inputNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(acceptNumber))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tapeSymb1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tapeSymb9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(moveToNextState)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(Auto))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1))
                            .addGap(31, 31, 31)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(currNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(currState))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(textPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(inputNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(acceptNumber))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tapeSymb1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tapeSymb5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(moveToNextState)
                            .addComponent(Auto))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(currState))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(currNumber)))
                    .addComponent(textPrinter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moveToNextStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moveToNextStateActionPerformed
       moveOneStep();
    }//GEN-LAST:event_moveToNextStateActionPerformed

    private void AutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AutoActionPerformed
        
        timer.start();
               
//        try {
//                       while(!currState.getText().equals("q3")){ 
//                      moveOneStep();         
//                      Thread.sleep(300);
//                      updateLabels();
//                       }
//                               //1000 milliseconds is one second.
//        } catch(InterruptedException ex) {
//                               Thread.currentThread().interrupt();
//        } 
      
        

        
        
//        Thread processThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                     
//            }
//        });
          
      
      
    }//GEN-LAST:event_AutoActionPerformed

    private void acceptNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptNumberActionPerformed
    checkIfValidNumber();
    printInputValidity();
    currState.setText("q0");
    stateHistory.setText("q0");
    if(inputValid){   
        makeTableWithInput();
        clearTape();
        headPosition=0;
        initializeTape();
        initCurrNumber();
        moveToNextState.setEnabled(true);
        Auto.setEnabled(true);
    } else{
        moveToNextState.setEnabled(false);
        Auto.setEnabled(false);
    }
    
    }//GEN-LAST:event_acceptNumberActionPerformed

    private void tapeSymb1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb1ActionPerformed

    private void tapeSymb2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb2ActionPerformed

    private void tapeSymb3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb3ActionPerformed

    private void tapeSymb4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb4ActionPerformed

    private void tapeSymb5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb5ActionPerformed

    private void tapeSymb6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb6ActionPerformed

    private void tapeSymb7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb7ActionPerformed

    private void tapeSymb8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb8ActionPerformed

    private void tapeSymb9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tapeSymb9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tapeSymb9ActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Auto;
    private javax.swing.JButton acceptNumber;
    private javax.swing.JLabel currNumber;
    private javax.swing.JLabel currState;
    private javax.swing.JTextField inputNumberText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JButton moveToNextState;
    private javax.swing.JTextArea stateHistory;
    private javax.swing.JTextField tapeSymb1;
    private javax.swing.JTextField tapeSymb2;
    private javax.swing.JTextField tapeSymb3;
    private javax.swing.JTextField tapeSymb4;
    private javax.swing.JTextField tapeSymb5;
    private javax.swing.JTextField tapeSymb6;
    private javax.swing.JTextField tapeSymb7;
    private javax.swing.JTextField tapeSymb8;
    private javax.swing.JTextField tapeSymb9;
    private javax.swing.JScrollPane textPrinter;
    // End of variables declaration//GEN-END:variables
}
