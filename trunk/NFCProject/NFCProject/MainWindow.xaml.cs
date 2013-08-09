using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Parse;
using Microsoft.Win32;
using System.IO;

namespace NFCProject 
{
  /// <summary>
  /// Interaction logic for MainWindow.xaml
  /// </summary>
  public partial class MainWindow : Window
  {
      List<ParseObject> m_RecentList, m_RegList;
      BitmapImage m_bitmap1, m_bitmap2;
      bool bSelectionRecent = false;
      int nSelectionIndex = -1;

    public MainWindow()
    {
      InitializeComponent();
    }

    private void frmMain_Activated(object sender, EventArgs e)
    {

    }

    private async void frmMain_Loaded(object sender, RoutedEventArgs e)
    {
        ParseAnalytics.TrackAppOpenedAsync();

        m_RecentList = new List<ParseObject>();
        m_RegList = new List<ParseObject>();

        //var testObject = new ParseObject("Windows");
        //testObject["WPF"] = "okay";
        //await testObject.SaveAsync();
        btn_regist.IsEnabled = false;
        btn_update.IsEnabled = false;


        await RefreshList();
        
        
    }

    private async Task RefreshList()
    {
        ClearLists();
        lst_Recent.Items.Add("Loading...");
        lst_Regist.Items.Add("Loading...");

        try
        {

            var query = ParseObject.GetQuery("NFC_List").WhereEqualTo("linked_user", "june");
            IEnumerable<ParseObject> results = await query.FindAsync();

            var query2 = ParseObject.GetQuery("NFC_reg").WhereEqualTo("linked_user", "june");
            IEnumerable<ParseObject> results2 = await query2.FindAsync();

            ClearLists();

            IEnumerator e1 = results.GetEnumerator();
            while (e1.MoveNext())
            {
                ParseObject obj = (ParseObject)(e1.Current);
                m_RecentList.Add(obj);
                lst_Recent.Items.Add(obj.Get<string>("NFC_id"));
            }
            lblRecentNFC.Content = "Recent (" + lst_Recent.Items.Count + ")";


            IEnumerator e2 = results2.GetEnumerator();
            while (e2.MoveNext())
            {
                ParseObject obj = (ParseObject)(e2.Current);
                m_RegList.Add(obj);
                lst_Regist.Items.Add(obj.Get<string>("NFC_id"));
            }
            lblRegNFC.Content = "Registered (" + lst_Regist.Items.Count + ")";
        }
        catch (Exception e)
        {
            MessageBox.Show("server connection exception");
        }
    }

    private void ClearLists()
    {
        try
        {
            m_RecentList.Clear();
            m_RegList.Clear();

            lst_Recent.Items.Clear();
            lst_Regist.Items.Clear();

        }
        catch (ArgumentOutOfRangeException e)
        {
            MessageBox.Show("refresh exception occured.\nretrying...");
        }
    }

    private async void btn_refresh_Click(object sender, RoutedEventArgs e)
    {
        await RefreshList();
    }

      // 선택이 바뀌면
    private void lst_Recent_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        bSelectionRecent = true;
        btn_regist.IsEnabled = true;   // 최근리스트 볼때는 등록
        btn_update.IsEnabled = false;    // 최근리스트시에 안함
        btn_remove.IsEnabled = true;    // 선택시 remove 가능

        if(lst_Recent.SelectedIndex!=-1)
            nSelectionIndex = lst_Recent.SelectedIndex;

        ParseObject obj= null;
        if (lst_Recent.SelectedIndex != -1 && m_RecentList.Count != 0)
            obj = m_RecentList[lst_Recent.SelectedIndex];

        if (obj == null)
            return;
        
        DateTime? timeCreated = obj.CreatedAt;
        txt_date.Text = timeCreated.Value.ToLocalTime().ToString();

        txt_id.Text= obj.Get<string>("NFC_id");
        txt_caption1.Text = "";

    }

       /// <summary>
       /// 등록된거 선택
       /// </summary>
       /// <param name="sender"></param>
       /// <param name="e"></param>
    private void lst_Regist_SelectionChanged(object sender, SelectionChangedEventArgs e)
    {
        bSelectionRecent = false;
        btn_regist.IsEnabled = false;   // 등록리스트 볼때는 등록할필요없음
        btn_update.IsEnabled = true;    // 등록시는 업데이트 버튼으로 업뎃함
        btn_remove.IsEnabled = true;    // 선택시 remove 가능

        if (lst_Regist.SelectedIndex != -1)
            nSelectionIndex = lst_Regist.SelectedIndex;

        ParseObject obj = null;
        if (lst_Regist.SelectedIndex != -1 && m_RegList.Count != 0)
            obj = m_RegList[lst_Regist.SelectedIndex];

        if (obj == null)
            return;

        DateTime? timeCreated = obj.CreatedAt;
        txt_date.Text = timeCreated.Value.ToLocalTime().ToString();

        txt_id.Text = obj.Get<string>("NFC_id");
        txt_caption1.Text = obj.Get<string>("Caption");
    }

      // 등록
    private async void btn_regist_Click_1(object sender, RoutedEventArgs e)
    {
        if (txt_caption1.Text.Equals(""))
        {
            MessageBoxResult result = MessageBox.Show("Please input caption.");
            return;
        }
        else if (txt_image1.Text.Equals("") || txt_image2.Text.Equals(""))
        {
            MessageBoxResult result = MessageBox.Show("Please select images.");
            return;
        }

        foreach(ParseObject PO in m_RegList)
        {
            if(txt_id.Text.Equals( PO.Get<string>("NFC_id") ))
            {
                MessageBoxResult result = MessageBox.Show("Current NFC id is already exist.");
                return;
            }
        }

        byte[] data1 = this.ConvertImageToByte(m_bitmap1);
        ParseFile file1 = new ParseFile("1.png", data1);
        await file1.SaveAsync();

        byte[] data2 = this.ConvertImageToByte(m_bitmap2);
        ParseFile file2 = new ParseFile("2.png", data2);
        await file2.SaveAsync();

        var NFCreg = new ParseObject("NFC_reg");
        NFCreg["NFC_id"] = txt_id.Text;
        NFCreg["linked_user"] = "june";
        NFCreg["Caption"] = txt_caption1.Text;
        NFCreg["File1"] = file1;
        NFCreg["File2"] = file2;
        await NFCreg.SaveAsync();

        MessageBox.Show("Regist finish!");

        RefreshList();
    }




    private async void btn_remove_Click_1(object sender, RoutedEventArgs e)
    {
        if (bSelectionRecent)
        {
            m_RecentList.RemoveAt(nSelectionIndex);
            lst_Recent.Items.RemoveAt(nSelectionIndex);

            ParseObject PO = m_RecentList[nSelectionIndex];            
            await PO.DeleteAsync();
        }
        else
        {
            m_RegList.RemoveAt(nSelectionIndex);
            lst_Regist.Items.RemoveAt(nSelectionIndex);

            ParseObject PO = m_RegList[nSelectionIndex];
            await PO.DeleteAsync();
        }
    }

    private async void btn_update_Click_1(object sender, RoutedEventArgs e)
    {
        if (txt_caption1.Text.Equals(""))
        {
            MessageBoxResult result = MessageBox.Show("Please input caption.");
            return;
        }
        else if (txt_image1.Text.Equals("") || txt_image2.Text.Equals(""))
        {
            MessageBoxResult result = MessageBox.Show("Please select images.");
            return;
        }


        byte[] data1 = this.ConvertImageToByte(m_bitmap1);
        ParseFile file1 = new ParseFile("1.png", data1);
        await file1.SaveAsync();

        byte[] data2 = this.ConvertImageToByte(m_bitmap2);
        ParseFile file2 = new ParseFile("2.png", data2);
        await file2.SaveAsync();

        var NFCreg = m_RegList[nSelectionIndex];
        NFCreg["NFC_id"] = txt_id.Text;
        NFCreg["linked_user"] = "june";
        NFCreg["Caption"] = txt_caption1.Text;
        NFCreg["File1"] = file1;
        NFCreg["File2"] = file2;
        await NFCreg.SaveAsync();

        MessageBox.Show("Update finish!");

        RefreshList();
    }

    private void SetImage(int nNumber)
    {
        OpenFileDialog openDialog = new OpenFileDialog();
        if (openDialog.ShowDialog() == true)
        {
            if (File.Exists(openDialog.FileName))
            {
                BitmapImage bitmapImage = new BitmapImage(new Uri(openDialog.FileName, UriKind.RelativeOrAbsolute));

                if (nNumber == 1)
                {
                    m_bitmap1 = bitmapImage;
                    img_1.Source = bitmapImage;
                    txt_image1.Text = openDialog.FileName;
                }
                if (nNumber == 2)
                {
                    m_bitmap2 = bitmapImage;
                    img_2.Source = bitmapImage;
                    txt_image2.Text = openDialog.FileName;
                }

            }
        }
    }

    private void btn_image1_Click(object sender, RoutedEventArgs e)
    {
        this.SetImage(1);
    }

    private void btn_image2_Click(object sender, RoutedEventArgs e)
    {
        this.SetImage(2);
    }

    private byte[] ConvertImageToByte(BitmapImage bitmap)
    {
        byte[] data;
        JpegBitmapEncoder encoder = new JpegBitmapEncoder();
        encoder.Frames.Add(BitmapFrame.Create(bitmap));
        using (MemoryStream ms = new MemoryStream())
        {
            encoder.Save(ms);
            data = ms.ToArray();
        }

        return data;
    }



  }
}
