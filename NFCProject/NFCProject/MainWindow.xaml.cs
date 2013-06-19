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

namespace WPFParseStarterProject {
  /// <summary>
  /// Interaction logic for MainWindow.xaml
  /// </summary>
  public partial class MainWindow : Window {
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

        //var testObject = new ParseObject("Windows");
        //testObject["WPF"] = "okay";
        //await testObject.SaveAsync();

        await RefreshList();
        
        
    }

    private async Task RefreshList()
    {
        int nCount= lst_Recent.Items.Count;
        for (int i = 0; i < nCount; ++i)
            lst_Recent.Items.RemoveAt(0);

        var query = ParseObject.GetQuery("NFC_List").WhereEqualTo("linked_user", "june");
        IEnumerable<ParseObject> results = await query.FindAsync();

        IEnumerator e1 = results.GetEnumerator();
        while (e1.MoveNext())
        {
            ParseObject obj = (ParseObject)(e1.Current);
            lst_Recent.Items.Add(obj.Get<string>("NFC_id"));
        }
    }

    private async void btn_refresh_Click(object sender, RoutedEventArgs e)
    {
        await RefreshList();
    }
  }
}
