using System;
using Xamarin.Forms;
using System.Globalization;
using Pokedex;

namespace PokemonApp.Converters
{
    public class TypeToColorConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value is string typeName && App.Current.Resources.TryGetValue($"TypeColor_{typeName.ToLower()}", out object color))
            {
                if (color is Color typedColor)
                {
                    return typedColor;
                }
            }

            return Color.LightGray;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
