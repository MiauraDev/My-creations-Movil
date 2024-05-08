using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Xamarin.Forms;

namespace PokemonApp.Views
{
    public partial class PokemonPage : ContentPage
    {
        private const string PokeApiUrl = "https://pokeapi.co/api/v2/pokemon?limit=1000";
        private HttpClient _httpClient;

        public ObservableCollection<Pokemon> Pokemons { get; private set; }

        public PokemonPage()
        {
            InitializeComponent();
            Pokemons = new ObservableCollection<Pokemon>();
            pokemonCollectionView.ItemsSource = Pokemons;
            _httpClient = new HttpClient();
            LoadPokemons();
        }

        private async void LoadPokemons()
        {
            try
            {
                HttpResponseMessage response = await _httpClient.GetAsync(PokeApiUrl);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    var pokemonListResult = JsonConvert.DeserializeObject<PokemonListResult>(content);

                    foreach (var pokemonInfo in pokemonListResult.Results)
                    {
                        Pokemon pokemon = await GetPokemonDetails(pokemonInfo.Url);
                        if (pokemon != null)
                        {
                            pokemon.ImageUrl = pokemon.Sprites.Other.OfficialArtwork?.FrontDefault ?? pokemon.Sprites.Other.DreamWorld.FrontDefault;
                            Pokemons.Add(pokemon);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                await DisplayAlert("Error", "Error al cargar los Pokémon: " + ex.Message, "OK");
            }
        }

        private async void OnPokemonSelected(object sender, SelectionChangedEventArgs e)
        {
            if (e.CurrentSelection.FirstOrDefault() is Pokemon selectedPokemon)
            {
                var pokemonDetailPage = new PokemonDetailPage(selectedPokemon);
                await Navigation.PushModalAsync(new NavigationPage(pokemonDetailPage));
            }

            ((CollectionView)sender).SelectedItem = null;
        }

        private async void OnSearchTextChanged(object sender, TextChangedEventArgs e)
        {
            string searchText = e.NewTextValue;
            if (string.IsNullOrWhiteSpace(searchText))
            {
                pokemonCollectionView.ItemsSource = Pokemons; // Show all Pokémon if search text is empty
            }
            else
            {
                var filteredPokemons = new ObservableCollection<Pokemon>(
                    Pokemons.Where(p => p.Name.StartsWith(searchText, StringComparison.OrdinalIgnoreCase)));
                pokemonCollectionView.ItemsSource = filteredPokemons; // Show filtered Pokémon by name
            }
        }

        private async Task<Pokemon> GetPokemonDetails(string pokemonUrl)
        {
            try
            {
                HttpResponseMessage response = await _httpClient.GetAsync(pokemonUrl);

                if (response.IsSuccessStatusCode)
                {
                    string content = await response.Content.ReadAsStringAsync();
                    var pokemonDetails = JsonConvert.DeserializeObject<Pokemon>(content);

                    return pokemonDetails;
                }
                else
                {
                    Console.WriteLine($"HTTP request failed with status code: {response.StatusCode}");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error while fetching Pokemon details: {ex}");
            }

            return null;
        }

        public class PokemonListResult
        {
            public List<PokemonInfo> Results { get; set; }
        }

        public class PokemonInfo
        {
            public string Name { get; set; }
            public string Url { get; set; }
        }
    }
}
