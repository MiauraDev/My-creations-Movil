using Newtonsoft.Json;

namespace PokemonApp.Views
{
    public class PokemonSprites
    {
        [JsonProperty("other")]
        public PokemonOtherSprites Other { get; set; }
    }

    public class PokemonOtherSprites
    {
        [JsonProperty("official-artwork")]
        public PokemonOfficialArtworkSprites OfficialArtwork { get; set; }
    }

    public class PokemonOfficialArtworkSprites
    {
        [JsonProperty("front_default")]
        public string FrontDefault { get; set; }
    }

}
