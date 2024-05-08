using Newtonsoft.Json;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;

public class PokemonService
{
    private HttpClient _client;

    public PokemonService()
    {
        _client = new HttpClient();
        _client.BaseAddress = new System.Uri("https://pokeapi.co/api/v2/");
    }

    public async Task<Pokemon> GetPokemonByIdAsync(int id)
    {
        HttpResponseMessage response = await _client.GetAsync($"pokemon/{id}/");

        if (response.IsSuccessStatusCode)
        {
            string json = await response.Content.ReadAsStringAsync();
            Pokemon pokemon = JsonConvert.DeserializeObject<Pokemon>(json);
            return pokemon;
        }
        else
        {
            return null;
        }
    }
}
