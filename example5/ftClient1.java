import java.io.*;
import java.util.*;
import javax.swing.table.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
class FileModel extends AbstractTableModel
{
private ArrayList<File> files;
FileModel()
{
this.files=new ArrayList<>();
}
public int getRowCount()
{
return this.files.size();
}
public int getColumnCount()
{
return 2;
}
public String getColumnName(int c)
{
if(c==0) return "S.No.";
return "File";
}
public Class getColumnClass(int c)
{
if(c==0) return Integer.class;
return String.class;
}
public boolean isCellEditable(int r,int c)
{
return false;
}
public Object getValueAt(int r,int c)
{
if(c==0)return (r+1);
return this.files.get(r).getAbsolutePath();
}
public void add(File file)
{
this.files.add(file);
fireTableDataChanged();
}
}
class FTClientFrame extends JFrame
{
private String host;
private int portNumber;
private FileSelectionPanel fileSelectionPanel;
private FileUploadViewPanel fileUploadViewPanel;
private Container container;
FTClientFrame(String host,int portNumber)
{
this.host=host;
this.portNumber=portNumber;
fileSelectionPanel= new FileSelectionPanel();
fileUploadViewPanel=new FileUploadViewPanel();
container=getContentPane();
container.setLayout(new GridLayout(1,2));
container.add(fileSelectionPanel);
container.add(fileUploadViewPanel);
setSize(1200,600);
setLocation(10,20);
setVisible(true);
}
public void setBytesUploaded(String id,long bytes)
{
}
public void fileUploaded(String id)
{
}
class FileSelectionPanel extends JPanel implements ActionListener
{
private JLabel titleLabel;
private FileModel model;
private JTable table;
private JScrollPane jsp;
private JButton addFileButton;
FileSelectionPanel()
{
setLayout(new BorderLayout());
titleLabel=new JLabel("Selected Files");
model=new FileModel();
table=new JTable(model);
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
addFileButton=new JButton("Add File");
addFileButton.addActionListener(this);
add(titleLabel,BorderLayout.NORTH);
add(jsp,BorderLayout.CENTER);
add(addFileButton,BorderLayout.SOUTH);
}
public void actionPerformed(ActionEvent ev)
{
JFileChooser jfc=new JFileChooser();
jfc.setCurrentDirectory(new File("."));
int selectedOption=jfc.showOpenDialog(this);
if(selectedOption==jfc.APPROVE_OPTION)
{
File selectedFile=jfc.getSelectedFile();
model.add(selectedFile);
}
}
}
class FileUploadViewPanel extends JPanel
{

}
public static void main(String gg[])
{
FTClientFrame fcf=new FTClientFrame("localhost",5500);
}
}
class FileUploadThread extends Thread
{
private FTClientFrame fcf;
private String id;
private File file;
private String host;
private int portNumber;
FileUploadThread(FTClientFrame fcf,String id,File file,String host,int portNumber)
{
this.fcf=fcf;
this.id=id;
this.file=file;
this.host=host;
this.portNumber=portNumber;
}
public void run()
{
try
{
long lengthOfFile=file.length();
String name=file.getName();
byte header[]=new byte[1024];
long x,k;
int i=0;
k=lengthOfFile;
while(k>0)
{
header[i]=(byte)(k%10);
k=k/10;
i++;
}
header[i]=(byte)',';
i++;
x=name.length();
int r=0;
while(r<x)
{
header[i]=(byte)name.charAt(r);
i++;
r++;
}
while(i<=1023)
{
header[i]=(byte)32;
i++;
}
Socket socket=new Socket("localhost",5500);
OutputStream os=socket.getOutputStream();
os.write(header,0,1024);//from which index,how many
os.flush();
InputStream is=socket.getInputStream();
byte ack[]=new byte[1];
int bytesReadCount;
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1)continue;
break;
}
FileInputStream fis=new FileInputStream(file);
int chunkSize=4096;
byte bytes[]=new byte[chunkSize];
int j=0;
while(j<lengthOfFile)
{
bytesReadCount=fis.read(bytes);
os.write(bytes,0,bytesReadCount);
os.flush();
j=j+bytesReadCount;
long brc=bytesReadCount;
SwingUtilities.invokeLater(()->{
fcf.setBytesUploaded(id,brc);
});
}
fis.close();
while(true)
{
bytesReadCount=is.read(ack);
if(bytesReadCount==-1)continue;
break;
}
socket.close();
SwingUtilities.invokeLater(()->{
fcf.fileUploaded(id);
});
}catch(Exception e)
{
System.out.println(e);
}
}
}