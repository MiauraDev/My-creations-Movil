using System;
using System.ComponentModel;
using System.Linq;
using Xamarin.Forms;

namespace PokemonApp.ViewModels
{
    public class PokemonDetailViewModel : INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private Pokemon _selectedPokemon;
        private PokemonStats _pokemonStats;

        public Pokemon SelectedPokemon
        {
            get { return _selectedPokemon; }
            set
            {
                if (_selectedPokemon != value)
                {
                    _selectedPokemon = value;
                    LoadPokemonStats();
                    OnPropertyChanged(nameof(SelectedPokemon));
                    OnPropertyChanged(nameof(PokemonStats)); // Notificar cambios en las estadísticas
                }
            }
        }

        public PokemonStats PokemonStats
        {
            get { return _pokemonStats; }
            set
            {
                _pokemonStats = value;
                OnPropertyChanged(nameof(PokemonStats));
            }
        }

        // Constructor que acepta un Pokémon
        public PokemonDetailViewModel(Pokemon pokemon)
        {
            SelectedPokemon = pokemon; // Establecer el Pokémon seleccionado
        }

        // Método para cargar las estadísticas del Pokémon seleccionado
        private void LoadPokemonStats()
        {
            if (SelectedPokemon != null && SelectedPokemon.Stats != null)
            {
                // Buscar las estadísticas requeridas y asignarlas a PokemonStats
                PokemonStats = new PokemonStats
                {
                    HP = GetStatValue("hp"),
                    Attack = GetStatValue("attack"),
                    Defense = GetStatValue("defense"),
                    SpecialAttack = GetStatValue("special-attack"),
                    SpecialDefense = GetStatValue("special-defense"),
                    Speed = GetStatValue("speed")
                };
            }
        }

        // Método para obtener el valor de una estadística específica
        private int GetStatValue(string statName)
        {
            var stat = SelectedPokemon.Stats.FirstOrDefault(s => s.Stat.Name == statName);
            return stat?.BaseStat ?? 0;
        }

        protected virtual void OnPropertyChanged(string propertyName)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}
