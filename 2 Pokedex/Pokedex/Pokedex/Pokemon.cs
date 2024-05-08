
using Newtonsoft.Json;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;

public class Pokemon
{
    [JsonProperty("id")]
    public int Id { get; set; }

    [JsonProperty("name")]
    public string Name { get; set; }

    [JsonProperty("types")]
    public List<PokemonType> Types { get; set; }

    [JsonProperty("height")]
    private int HeightCm { get; set; }

    [JsonIgnore]
    public double HeightM => HeightCm / 10.0;

    [JsonProperty("weight")]
    private int WeightG { get; set; }

    [JsonIgnore]
    public double WeightKg => WeightG / 10.0;

    [JsonProperty("sprites")]
    public PokemonSprites Sprites { get; set; }

    [JsonProperty("stats")]
    public List<PokemonStat> Stats { get; set; } // Lista de estadísticas del Pokémon

    public string ImageUrl { get; internal set; }

    public Pokemon()
    {
        Types = new List<PokemonType>();
        Stats = new List<PokemonStat>();
    }

    // Método estático para obtener un Pokémon por ID desde la PokeAPI
    public static async Task<Pokemon> GetPokemonByIdAsync(int pokemonId)
    {
        using (HttpClient client = new HttpClient())
        {
            HttpResponseMessage response = await client.GetAsync($"https://pokeapi.co/api/v2/pokemon/{pokemonId}/");
            if (response.IsSuccessStatusCode)
            {
                string json = await response.Content.ReadAsStringAsync();
                Pokemon pokemon = JsonConvert.DeserializeObject<Pokemon>(json);

                // Si las estadísticas se cargan correctamente, asignar el nombre de la estadística
                if (pokemon.Stats != null && pokemon.Stats.Count > 0)
                {
                    foreach (var stat in pokemon.Stats)
                    {
                        switch (stat.Stat.Name)
                        {
                            case "hp":
                                stat.Stat.Name = "HP";
                                break;
                            case "attack":
                                stat.Stat.Name = "Attack";
                                break;
                            case "defense":
                                stat.Stat.Name = "Defense";
                                break;
                            case "special-attack":
                                stat.Stat.Name = "Special Attack";
                                break;
                            case "special-defense":
                                stat.Stat.Name = "Special Defense";
                                break;
                            case "speed":
                                stat.Stat.Name = "Speed";
                                break;
                            default:
                                break;
                        }
                    }
                }

                return pokemon;
            }
            else
            {
                throw new HttpRequestException($"Error al obtener datos del Pokémon con ID {pokemonId}");
            }
        }
    }
}

public class PokemonType
{
    [JsonProperty("type")]
    public PokemonTypeName Type { get; set; }
}

public class PokemonTypeName
{
    [JsonProperty("name")]
    public string Name { get; set; }
}

public class PokemonSprites
{
    [JsonProperty("front_default")]
    public string FrontDefault { get; set; }

    [JsonProperty("other")]
    public OtherSprites Other { get; set; }
}

public class OtherSprites
{
    [JsonProperty("dream_world")]
    public DreamWorldSprites DreamWorld { get; set; }

    [JsonProperty("home")]
    public HomeSprites Home { get; set; }

    [JsonProperty("official-artwork")]
    public OfficialArtwork OfficialArtwork { get; set; }
}

public class DreamWorldSprites
{
    [JsonProperty("front_default")]
    public string FrontDefault { get; set; }
}

public class HomeSprites
{
    [JsonProperty("front_default")]
    public string FrontDefault { get; set; }
}

public class OfficialArtwork
{
    [JsonProperty("front_default")]
    public string FrontDefault { get; set; }
}

public class PokemonStat
{
    [JsonProperty("stat")]
    public PokemonStatName Stat { get; set; }

    [JsonProperty("base_stat")]
    public int BaseStat { get; set; }
}

public class PokemonStatName
{
    [JsonProperty("name")]
    public string Name { get; set; }
}
