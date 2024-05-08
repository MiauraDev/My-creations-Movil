using Xamarin.Forms;
using System.Linq;

namespace PokemonApp.Views
{
    public class PokemonTemplateSelector : DataTemplateSelector
    {
        public DataTemplate FirstTemplate { get; set; }
        public DataTemplate MiddleTemplate { get; set; }
        public DataTemplate LastTemplate { get; set; }

        protected override DataTemplate OnSelectTemplate(object item, BindableObject container)
        {
            var itemsView = (CollectionView)container;
            var itemsList = itemsView.ItemsSource.Cast<object>().ToList();
            var index = itemsList.IndexOf(item);

            if (index == 0)
                return FirstTemplate;
            else if (index == itemsList.Count - 1)
                return LastTemplate;
            else
                return MiddleTemplate;
        }
    }
}
