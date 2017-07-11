package com.liwenzhi.dcm;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.liwenzhi.dcm.photoview.PhotoView;
import org.dcm4che3.android.Raster;
import org.dcm4che3.android.RasterUtil;
import org.dcm4che3.android.imageio.dicom.DicomImageReader;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;

import java.io.*;

/**
 * 解析并显示dicom文件的示例
 */
public class MyActivity extends Activity {

    PhotoView iv;
    TextView tv_picture;
    TextView tv_name;
    TextView tv_birthday;
    TextView tv_institution;
    TextView tv_station;
    TextView tv_StudyDescription;
    TextView tv_SeriesDescription;
    TextView tv_manufacturerModelName;
    TextView tv_manufacturer;
    TextView tv_StudyDate;
    int number = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initView();
        initEvent();
    }

    private void initView() {
        iv = (PhotoView) findViewById(R.id.iv);
        tv_picture = (TextView) findViewById(R.id.tv_picture);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_institution = (TextView) findViewById(R.id.tv_institution);
        tv_station = (TextView) findViewById(R.id.tv_station);
        tv_StudyDescription = (TextView) findViewById(R.id.tv_StudyDescription);
        tv_SeriesDescription = (TextView) findViewById(R.id.tv_SeriesDescription);
        tv_manufacturer = (TextView) findViewById(R.id.tv_manufacturer);
        tv_manufacturerModelName = (TextView) findViewById(R.id.tv_manufacturerModelName);
        tv_StudyDate = (TextView) findViewById(R.id.tv_StudyDate);

    }


    private void initEvent() {


    }


    public void next(View view) {
        //清空数据
        tv_name.setText("");
        tv_birthday.setText("");
        tv_institution.setText("");
        tv_station.setText("");
        tv_StudyDescription.setText("");
        tv_manufacturer.setText("");
        tv_manufacturerModelName.setText("");
        tv_StudyDate.setText("");
        tv_SeriesDescription.setText("");
        iv.setImageResource(R.drawable.ic_launcher);

        String fileName = "test" + number + ".dcm";
        tv_picture.setText(fileName);
        //加载图片
        btnLoadClicked(fileName);

        number++;
        if (number == 10)
            number = 1;

    }


    void btnLoadClicked(String fileName) {
        String testFileName = this.getCacheDir().getAbsolutePath() + "/" + fileName;
        File file = new File(testFileName);
        if (file.exists())
            file.delete();
        InputStream is;
        try {
            is = getAssets().open(fileName);
            copyFile(is, file);      //从assets中复制到本地路径
            readFile(testFileName); //读取本地路径中的dicom文件
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 复制文件的代码
     */
    void copyFile(InputStream is, File dstFile) {
        try {

            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(dstFile), 1024);
            byte buf[] = new byte[1024];
            int c = 0;
            c = bis.read(buf);
            while (c > 0) {
                bos.write(buf, 0, c);
                c = bis.read(buf);
            }
            bis.close();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取文件数据
     */
    public void readFile(String filePath) {
        DicomImageReader dr = new DicomImageReader();
        try {
            File file = new File(filePath);
            //dcm文件输入流
            DicomInputStream dcmInputStream = new DicomInputStream(file);
            //属性对象
            Attributes attrs = dcmInputStream.readDataset(-1, -1);
            //输出所有属性信息
            Log.e("TAG", "输出所有属性信息1:" + attrs);

            //获取行
            int row = attrs.getInt(Tag.Rows, 1);
            //获取列
            int columns = attrs.getInt(Tag.Columns, 1);
            //窗宽窗位
            float win_center = attrs.getFloat(Tag.WindowCenter, 1);
            float win_width = attrs.getFloat(Tag.WindowWidth, 1);
            Log.e("TAG", "" + "row=" + row + ",columns=" + row + "row*columns = " + row * columns);

            Log.e("TAG", "" + "win_center=" + win_center + ",win_width=" + win_width);
            //获取像素数据 ，这个像素数据不知道怎么用！！！，得到的是图片像素的两倍的长度
            //后面那个 raster.getByteData()是图片的像素数据
            byte[] b = attrs.getSafeBytes(Tag.PixelData);
            if (b != null) {
                Log.e("TAG", "" + "b.length=" + b.length);
            } else {
                Log.e("TAG", "" + "b==null");
            }

            //修改默认字符集为GB18030
            attrs.setString(Tag.SpecificCharacterSet, VR.CS, "GB18030");//解决中文乱码问题

            Log.e("TAG", "输出所有属性信息2:" + attrs);
            String patientName = attrs.getString(Tag.PatientName, "");
            tv_name.setText("姓名：" + patientName);

            //生日
            String patientBirthDate = attrs.getString(Tag.PatientBirthDate, "");
            tv_birthday.setText("生日：" + patientBirthDate);

            //机构
            String institution = attrs.getString(Tag.InstitutionName, "");
            tv_institution.setText("机构：" + institution);

            //站点
            String station = attrs.getString(Tag.StationName, "");
            tv_station.setText("站点：" + station);

            //制造商
            String Manufacturer = attrs.getString(Tag.Manufacturer, "");
            tv_manufacturer.setText("制造商：" + Manufacturer);

            //制造商模型
            String ManufacturerModelName = attrs.getString(Tag.ManufacturerModelName, "");
            tv_manufacturerModelName.setText("制造商模型：" + ManufacturerModelName);


            //描述--心房
            String description = attrs.getString(Tag.StudyDescription, "");
            tv_StudyDescription.setText("描述--心房：" + description);
            //描述--具体
            String SeriesDescription = attrs.getString(Tag.SeriesDescription, "");
            tv_SeriesDescription.setText("描述--具体：" + SeriesDescription);

            //描述时间
            String studyData = attrs.getString(Tag.StudyDate, "");
            tv_StudyDate.setText("描述时间：" + studyData);


            dr.open(file);
//            Attributes ds = dr.getAttributes();
//            String wc = ds.getString(Tag.WindowCenter);
//            String ww = ds.getString(Tag.WindowWidth);
//            Log.e("TAG", "" + "wc=" + wc + ",ww=" + ww);
            Raster raster = dr.applyWindowCenter(0, (int) win_width, (int) win_center);
//            Log.e("TAG", "" + "raster.getWidth()=" + raster.getWidth() + ",raster.getHeight()=" + raster.getHeight());
//            Log.e("TAG", "" + "raster.getByteData().length=" + raster.getByteData().length);

//            Bitmap bmp = RasterUtil.gray8ToBitmap(raster.getWidth(), raster.getHeight(), raster.getByteData());
//            Log.e("TAG", "b==raster.getByteData()" + (b == raster.getByteData()));
            Bitmap bmp = RasterUtil.gray8ToBitmap(columns, row, raster.getByteData());
            iv.setImageBitmap(bmp); //显示图片

        } catch (Exception e) {
            Log.e("TAG", "" + e);
        }

    }


}
