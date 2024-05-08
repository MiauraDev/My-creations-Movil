using PokemonApp.ViewModels;
using System;
using System.Threading.Tasks;
using Xamarin.Forms;

namespace PokemonApp.Views
{
    public partial class PokemonDetailPage : ContentPage
    {
        private readonly PokemonDetailViewModel viewModel;

        public PokemonDetailPage(Pokemon selectedPokemon)
        {
            InitializeComponent();

            // Asignar el contexto de vista con el ViewModel
            BindingContext = new PokemonDetailViewModel(selectedPokemon);

            // Llamar a la función para iniciar la animación cuando la página aparezca
            this.Appearing += PokemonDetailPage_Appearing;
            _ = StartImageAnimation();
        }

        private async void PokemonDetailPage_Appearing(object sender, EventArgs e)
        {
            // Verificar si el contexto de datos es del tipo correcto (PokemonDetailViewModel)
            if (BindingContext is PokemonDetailViewModel viewModel)
            {
                // Esperar un breve momento para asegurarse de que el contexto de datos se haya establecido completamente
                await Task.Delay(200);

                // Llamada a la animación para cada barra de progreso después de asignar el valor
                await AnimateStatBar(HPBarView, viewModel.SelectedPokemon.Stats[0].BaseStat);
                await AnimateStatBar(AttackBarView, viewModel.SelectedPokemon.Stats[1].BaseStat);
                await AnimateStatBar(DefenseBarView, viewModel.SelectedPokemon.Stats[2].BaseStat);
            }
        }

        public async Task StartImageAnimation()
        {
            while (true) // Bucle infinito para la animación continua
            {
                uint duration = 2000; // Duración total de la animación en milisegundos
                double minScale = 0.8; // Escala mínima
                double maxScale = 1.2; // Escala máxima

                // Animación de escala para simular el efecto de respiración
                await pokemonImage.ScaleTo(maxScale, duration, Easing.SinOut);
                await pokemonImage.ScaleTo(minScale, duration, Easing.SinIn);
            }
        }


        private async Task AnimateStatBar(BoxView barView, int baseStatValue)
        {
            uint animationDuration = 1000; // Duración de la animación en milisegundos
            double maxWidth = 180; // Ancho máximo de la barra (ajusta según tus necesidades)

            // Ajustar el valor de la estadística para que esté dentro del rango de 0 a 255
            double adjustedValue = Math.Min(Math.Max(baseStatValue, 0), 255);

            // Calcula el ancho proporcional de la barra según el valor de la estadística ajustada
            double finalWidth = (adjustedValue / 255.0) * maxWidth;

            // Animar la barra de progreso
            await barView.LayoutTo(new Rectangle(barView.Bounds.X, barView.Bounds.Y, 0, barView.Height), animationDuration); // Comienza desde un ancho de cero
            await barView.LayoutTo(new Rectangle(barView.Bounds.X, barView.Bounds.Y, finalWidth, barView.Height), animationDuration); // Crece hasta el ancho final
        }
    }
}
