using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Essentials;
using Xamarin.Forms.Xaml;
using PokemonApp.Views;

namespace pokeapi
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class NoInternetPage : ContentPage
    {
        public NoInternetPage()
        {
            InitializeComponent();
            BindingContext = this;
        }

        public Command RecheckConnectionCommand => new Command(async () =>
        {
            if (Connectivity.NetworkAccess == NetworkAccess.Internet)
            {
                // Si hay conexión, cambiar a la página principal
                Application.Current.MainPage = new NavigationPage(new PokemonPage());
            }
            else
            {
                // Si sigue sin conexión, mostrar mensaje de error nuevamente
                await DisplayAlert("Error", "No se puede conectar a Internet. Intente nuevamente más tarde.", "OK");
            }
        });
    }
}