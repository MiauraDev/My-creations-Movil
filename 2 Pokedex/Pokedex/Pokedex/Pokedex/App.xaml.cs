using pokeapi;
using PokemonApp.Views;
using System;
using System.Threading.Tasks;
using Xamarin.Essentials;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;


namespace Pokedex
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            // Usar NavigationPage para manejar correctamente la navegación
            MainPage = new NavigationPage(new SplashPage());
        }

        protected override async void OnStart()
        {
            // Esperar 5 segundos para simular la carga
            await Task.Delay(5000);

            // Verificar la conectividad a Internet
            if (Connectivity.NetworkAccess == NetworkAccess.Internet)
            {
                // Si hay conexión, cambiar a la página principal
                MainPage = new NavigationPage(new PokemonPage());
            }
            else
            {
                // Si no hay conexión, cambiar a la página de no conexión a internet
                MainPage = new NavigationPage(new NoInternetPage());
            }
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}
