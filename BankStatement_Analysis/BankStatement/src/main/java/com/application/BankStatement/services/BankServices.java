package com.application.BankStatement.services;

import com.application.BankStatement.entity.Statement;
import com.application.BankStatement.entity.User;
import com.application.BankStatement.repo.StatementRepo;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.application.BankStatement.repo.UserRepo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class BankServices {

    @Autowired
    UserRepo userRepo;
    @Autowired
    StatementRepo statementRepo;

    public User addUser(User user, MultipartFile file)throws IOException {
        user.setFilename(file.getName());
        user.setFiletype(file.getContentType());
        user.setFileData(file.getBytes());

        return userRepo.save(user);
    }

    public List<Statement> readFile(int id) throws IOException {

        User user= null;
        String text= "";
        Optional<User> OpUser = userRepo.findById(id);

        if(OpUser.isPresent())
        {
            user = OpUser.get();
            PDDocument document = Loader.loadPDF(user.getFileData());
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(document);
            document.close();
            text = removeDuplicate(text);
        }
        return parsePDF(text);
    }

    public String removeDuplicate(String text){

        String RegexDuplicateSubString = text.substring(0,text.indexOf("Brought Forward")+"Brought Forward".length()).replace("Page No. : 1","Page No. : [0-9]+");
        String filteredPDF = text.replaceAll(RegexDuplicateSubString,"");
        RegexDuplicateSubString = "(?is)Carried Forward\\s+\\d+(\\.\\d+)?[Cc]r\\s+" +
                "Statement\\s+" +
                "Summary Dr\\. Count:\\d+ Cr\\. Count:\\d+\\s+\\d+(\\.\\d+)?\\s+\\d+(\\.\\d+)?\\s+" +
                "In Case Your Account Is Operated By A Letter Of Authority/Power Of Attorney Holder, Please Check The\\s+" +
                "Transaction With Extra Care\\.\\s+" +
                "\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}\\s+[APap][Mm]\\s+about:blank\\s+" +
                "about:blank \\d+/\\d+\\s+" +
                "\\d+(\\.\\d+)?[Cc]r";

        filteredPDF = filteredPDF.replaceAll(RegexDuplicateSubString,"");

        RegexDuplicateSubString = "(?is)CLOSING BALANCE\\s*:\\s*\\d+(\\.\\d+)?[Cc]r\\s+" +
                "Statement\\s+" +
                "Summary\\s+" +
                "Dr\\.\\s+" +
                "Count:\\d+\\s+Cr\\.\\s*Count:\\d+\\s+\\d+(\\.\\d+)?\\s+\\d+(\\.\\d+)?\\s+" +
                "In Case Your Account Is Operated By A Letter Of Authority/Power Of Attorney Holder, Please Check The\\s+" +
                "Transaction With Extra Care\\.\\s+" +
                "\\*\\*\\* END OF STATEMENT \\*\\*\\*\\s+" +
                "\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}\\s+[APap][Mm]\\s+about:blank\\s+" +
                "about:blank \\d+/\\d+";

        filteredPDF = filteredPDF.replaceAll(RegexDuplicateSubString,"");

        RegexDuplicateSubString = "(?is)" +
                // Carried Forward block
                "Carried Forward\\s+\\d+(\\.\\d+)?[Cc]r\\s+" +
                "Statement\\s+" +
                "Summary\\s*Dr\\.\\s*Count:\\d+\\s+Cr\\.\\s*Count:\\d+\\s+\\d+(\\.\\d+)?\\s+\\d+(\\.\\d+)?\\s+" +
                "In Case Your Account Is Operated By A Letter Of Authority/Power Of Attorney Holder, Please Check The\\s+" +
                "Transaction With Extra Care\\.\\s+" +
                // Any number of date + about:blank lines
                "(\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}\\s+[APap][Mm]\\s+about:blank\\s+about:blank \\d+/\\d+\\s*)*" +
                "|" +
                // OR just the page footer lines
                "(\\d{1,2}/\\d{1,2}/\\d{2,4},\\s+\\d{1,2}:\\d{2}\\s+[APap][Mm]\\s+about:blank\\s*about:blank \\d+/\\d+\\s*)";

        filteredPDF = filteredPDF.replaceAll(RegexDuplicateSubString,"");

        return filteredPDF;
    }

    public List<Statement> parsePDF(String text) {

        List<Statement> statements = new ArrayList<Statement>();

        final double[] finalTotalBalnce = new double[1];
        finalTotalBalnce[0] = Double.parseDouble((text.substring(1, text.indexOf("cr"))));

        text = text.substring(text.indexOf("cr")+2);
        List<String> Transactions = Arrays.stream(text.split("Cr")).toList();
      try{
        Transactions.forEach(
                Transaction-> {

                Statement eachstatement=new Statement();
              //  SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

                List<String> part= Arrays.stream(Transaction.trim().split("\\s+")).toList();

                    eachstatement.setPostdate(part.get(0));
                    eachstatement.setValuedate(part.get(1));
                    eachstatement.setAmount(Double.parseDouble(part.get(part.size()-2)));
                    eachstatement.setBalance(Double.parseDouble(part.get(part.size()-1)));

                    String Details = "";
                    for(int i =2;i<part.size()-1;i++)
                    {
                        Details = Details+part.get(i);
                    }


                    eachstatement.setDetails(Details);
                    try{
                    eachstatement.setAccountName(Details.split("/")[1]);}
                    catch(Exception e){
                        eachstatement.setAccountName(Details);
                    }

                    if( finalTotalBalnce[0] > eachstatement.getBalance()){eachstatement.setType("Debited");}
                   else{eachstatement.setType("Credited");}

                    finalTotalBalnce[0] = eachstatement.getBalance();
                    statements.add(eachstatement);
            }
        );}
      catch(Exception e){System.out.println(e.getMessage());}

        statementRepo.saveAll(statements);
      return statements;
    }

    public List<Statement> findAccountBy(String name) {
        return userRepo.findByName(name);
    }
}
