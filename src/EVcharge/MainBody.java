package EVcharge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.ui.RefineryUtilities;

public class MainBody{
	
	static EV[] evUsers = null;
	static ChPoint[] chPoint = null;
	
    private static void split(EV[] tempUsers,int lowerIndex, int higherIndex,int chType) {
        
        if (lowerIndex < higherIndex) {
            int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
            split(tempUsers,lowerIndex, middle,chType);
            split(tempUsers,middle + 1, higherIndex,chType);
            merge(tempUsers,lowerIndex, middle, higherIndex,chType);
        }
    }
 
    private static void merge(EV[] tempUsers,int lowerIndex, int middle, int higherIndex,int chType) {
    	EV[] tempMergArr = new EV[higherIndex-lowerIndex+1];
        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i-lowerIndex] = tempUsers[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i-lowerIndex].getChargingTime(chType) <= tempMergArr[j-lowerIndex].getChargingTime(chType)) {
            	tempUsers[k] = tempMergArr[i-lowerIndex];
                i++;
            } else {
            	tempUsers[k] = tempMergArr[j-lowerIndex];
                j++;
            }
            k++;
        }
        while (i <= middle) {
        	tempUsers[k] = tempMergArr[i-lowerIndex];
            k++;
            i++;
        }
 
    }
	
	private static void sort(EV[] tempUsers,int totalNum, int chType){
		split(tempUsers,0,totalNum-1,chType);
	}
	
	private static void allocate(ChPoint[] chPoint,EV[] tempUsers,int totalNum, int chType){
		sort(tempUsers,totalNum,chType);
		for (int i=0;i<totalNum;i++){
			int time = tempUsers[i].getChargingTime(chType);
			int startPoint=tempUsers[i].getStartHour()*60+tempUsers[i].getStartMinute();
			int stopPoint=tempUsers[i].getStopHour()*60+tempUsers[i].getStopMinute();
			schedule plan = null;
			for (int j=0;j<chPoint.length;j++){
				if (chPoint[j]==null) continue;
				if (chPoint[j].getChargingTypeID()==chType){
					boolean startFlag=false;
					int sTime=0,eTime;
					for (int k=0;k<1440;k++){
						if (!startFlag){
							if (k>stopPoint-time) break;
							if(chPoint[j].getTimeSlot(k)==0){
								startFlag=true;
								sTime=k;
							}
						}
						else {
							if (chPoint[j].getTimeSlot(k)>0){
								startFlag=false;
								eTime=k;
								if (eTime-sTime>=time&&eTime>=startPoint+time){
									if (plan==null)
										plan = new schedule(j,Math.max(startPoint,sTime),eTime-sTime);
									else if (plan.getTotalInterval()>eTime-sTime){
										plan.setChSerial(j);
										plan.setTotalInterval(eTime-sTime);
										plan.setStartPoint(Math.max(startPoint,sTime));
									}
								}
							}
						}
					}
					if (startFlag){
						startFlag=false;
						eTime=1440;
						if (eTime-sTime>=time&&eTime>=startPoint+time){
							if (plan==null)
								plan = new schedule(j,Math.max(startPoint,sTime),eTime-sTime);
							else if (plan.getTotalInterval()>eTime-sTime){
								plan.setChSerial(j);
								plan.setTotalInterval(eTime-sTime);
								plan.setStartPoint(Math.max(startPoint,sTime));
							}
						}
					}
				}	
			}
			if (plan!=null) {
				tempUsers[i].setChargingPoint(chPoint[plan.getChSerial()].getID());
				for (int j=0;j<time;j++)
					chPoint[plan.getChSerial()].setTimeSlot(plan.getStartPoint()+j,tempUsers[i].getUserID());				
			}
		}	
	}

	public static void allocate(){
		EV[] filtedEVs = new EV[evUsers.length];
		int index=0;
		for (int i=0;i<evUsers.length;i++){
			if (evUsers[i]!=null && evUsers[i].getChargingTime(4)>0){
				filtedEVs[index++]=evUsers[i];
			}
		}
		if (index>0) allocate(chPoint,filtedEVs,index, 4);
		
		index=0;
		for (int i=0;i<evUsers.length;i++){
			if (evUsers[i]!=null && evUsers[i].getChargingTime(3)>0){
				filtedEVs[index++]=evUsers[i];
			}
		}
		if (index>0) allocate(chPoint,filtedEVs,index, 3);
		
		index=0;
		for (int i=0;i<evUsers.length;i++){
			if (evUsers[i]!=null && evUsers[i].getChargingTime(2)>0
				&& evUsers[i].getChargingPoint()==0){
				filtedEVs[index++]=evUsers[i];
			}
		}
		if (index>0) allocate(chPoint,filtedEVs,index, 2);	
		
		index=0;
		for (int i=0;i<evUsers.length;i++){
			if (evUsers[i]!=null && evUsers[i].getChargingTime(1)>0
				&& evUsers[i].getChargingPoint()==0){
				filtedEVs[index++]=evUsers[i];
			}
		}
		if (index>0) allocate(chPoint,filtedEVs,index, 1);	
	}
	
	
	public static int findEVbyID(int id){
		int result = -1;
		for (int i=0;i<evUsers.length;i++){
			if (evUsers[i]!=null && evUsers[i].getUserID()==id){
				result = i;
				break;
			}
		}
		return result;
	}
	
	public static int findChPointbyID(int id){
		int result = -1;
		for (int i=0;i<chPoint.length;i++){
			if (chPoint[i]!=null&&chPoint[i].getID()==id){
				result = i;
				break;
			}
		}
		return result;
	}
	
	
	public static void main(String argu[]){
		String inputFileName;
		JFileChooser chooser;
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("./"));
		chooser.setDialogTitle("Choose excel file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileFilter(){
			@Override
			public boolean accept(File f){
				if(f.getName().endsWith(".xlsx")||f.isDirectory())
					return true;
				else return false;
			}
			public String getDescription(){
				return "excel (*.xlsx)";
			}
		});
		if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}			
		inputFileName = chooser.getSelectedFile().getAbsolutePath().trim();//
		if (inputFileName.isEmpty()){
			JOptionPane.showMessageDialog(null,"File name invalid");
		}

		FileInputStream file=null;
		XSSFWorkbook workbook=null;

		try{
			file = new FileInputStream(new File(inputFileName));
			workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rowNum = sheet.getPhysicalNumberOfRows();
			evUsers = new EV[rowNum-1];
			for (int i=1;i<rowNum;i++){
				Date start,stop;
				int id, starthour,startminute,
					stophour,stopminute,
					miles, evType;
				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				id = (int)cell.getNumericCellValue();
				cell = row.getCell(1); 
				start = cell.getDateCellValue();
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
				starthour = Integer.parseInt(dateFormat.format(start));
				dateFormat = new SimpleDateFormat("mm");
				startminute = Integer.parseInt(dateFormat.format(start));;
				cell = row.getCell(2); 
				stop = cell.getDateCellValue();
				dateFormat = new SimpleDateFormat("HH");
				stophour = Integer.parseInt(dateFormat.format(stop));
				dateFormat = new SimpleDateFormat("mm");
				stopminute = Integer.parseInt(dateFormat.format(stop));;
				
				cell = row.getCell(3);
				miles = (int)cell.getNumericCellValue();
		
				cell = row.getCell(4);
				evType = (int)cell.getNumericCellValue();
				evUsers[i-1] = new EV(id,evType,starthour,startminute,stophour,stopminute,miles);
			}
			
			sheet = workbook.getSheetAt(1);
			rowNum = sheet.getPhysicalNumberOfRows();
			chPoint = new ChPoint[rowNum-1];
			for (int i=1;i<rowNum;i++){
				int id, chType;
				XSSFRow row = sheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				id = (int)cell.getNumericCellValue();
				cell = row.getCell(1); 
				chType = (int)cell.getNumericCellValue();
				chPoint[i-1] = new ChPoint(id,chType);
			}
			allocate();
			int index = 0;
			for (int i=0;i<evUsers.length;i++){
				if (evUsers[i]!=null && evUsers[i].getChargingPoint()>0){
					index++;
				}
			}
			System.out.println(index);
			
			sheet=workbook.getSheet("lzj output");
			if (sheet==null) sheet = workbook.createSheet("lzj output");
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("EV userID");
			cell = row.createCell(1);
			cell.setCellValue("EV type");
			cell = row.createCell(2);
			cell.setCellValue("Start");						
			cell = row.createCell(3);
			cell.setCellValue("Duratin");						
			cell = row.createCell(4);
			cell.setCellValue("Stop");
			cell = row.createCell(5);
			cell.setCellValue("Charger ID");						
			cell = row.createCell(6);
			cell.setCellValue("Charger type");
			rowNum=1;
			for (int i=0;i<chPoint.length;i++){
				if (chPoint[i]==null) continue;
				int userID = 0;
				Boolean flag = false;
				int stTime = 0;
				CellStyle cellStyle = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyle.setDataFormat(
				    createHelper.createDataFormat().getFormat("HH:mm"));
				for (int j=0;j<1440;j++){
					if (!flag){ 
						if (chPoint[i].getTimeSlot(j)>0){
							flag =true;
							userID = chPoint[i].getTimeSlot(j);
							stTime = j;
						}
					}
					else {
						if (chPoint[i].getTimeSlot(j)!=userID){
							row = sheet.createRow(rowNum++);
							cell = row.createCell(0);
							cell.setCellValue(userID);
							cell = row.createCell(1);
							cell.setCellValue(evUsers[findEVbyID(userID)].getEVtype());
							cell = row.createCell(2);
							cell.setCellValue(new Date((stTime+5L*60)*60000L));
							cell.setCellStyle(cellStyle);
							cell = row.createCell(3);
							cell.setCellValue((j-stTime)+" M");						
							cell = row.createCell(4);
							cell.setCellValue(new Date((j+5L*60)*60000L));
							cell.setCellStyle(cellStyle);
							cell = row.createCell(5);
							cell.setCellValue(chPoint[i].getID());						
							cell = row.createCell(6);
							cell.setCellValue(chPoint[i].getChargingType());
							if (chPoint[i].getTimeSlot(j)>0){
								userID = chPoint[i].getTimeSlot(j);
								stTime=j;
							}
							else {
								flag=false;
							}
						}
					}	
				}
				if (flag){
					row = sheet.createRow(rowNum++);
					cell = row.createCell(0);
					cell.setCellValue(userID);
					cell = row.createCell(1);
					cell.setCellValue(evUsers[findEVbyID(userID)].getEVtype());
					cell = row.createCell(2);
					cell.setCellValue(new Date((stTime+5L*60)*60000L));						
					cell = row.createCell(3);
					cell.setCellValue(1440-stTime);						
					cell = row.createCell(4);
					cell.setCellValue(new Date((1440+5L*60)*60000L));
					cell = row.createCell(5);
					cell.setCellValue(chPoint[i].getID());						
					cell = row.createCell(6);
					cell.setCellValue(chPoint[i].getChargingType());
				}
			
			}	
	        try (OutputStream fileOut = new FileOutputStream("output"+System.currentTimeMillis()+".xlsx")) {
	        	workbook.write(fileOut);
	        }
			
	        Chart chart = new Chart("EV Charging", chPoint);
	        chart.pack();
	        RefineryUtilities.centerFrameOnScreen(chart);
	        chart.setVisible(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (workbook!=null) workbook.close();
				if (file != null)file.close();
			} catch (IOException ex) {
			}
		} 
	}
}
