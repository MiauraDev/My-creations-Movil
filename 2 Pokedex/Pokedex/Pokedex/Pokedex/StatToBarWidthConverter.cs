using System;
using System.Globalization;
using Xamarin.Forms;

namespace PokemonApp.Converters
{
    public class StatToBarWidthConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value is int baseStat)
            {
                double maxWidth = 200; // Ancho máximo deseado para la barra
                double adjustedValue = Math.Min(Math.Max(baseStat, 0), 255); // Ajustar el valor entre 0 y 255
                return (adjustedValue / 255.0) * maxWidth; // Calcular el ancho proporcional
            }

            return 0; // Valor predeterminado en caso de error
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
