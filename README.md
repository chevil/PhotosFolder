# Photos Folder app for a test
This app download a photos list,
using retrofit and rxjava
and pass it to the Lazy loading (Picasso) for showing them in a list

The photos list is also saved in a file with serialization
and the images are cached by Picasso,
so that , when offline, the list is restored
from the saved data ( deserialization )
and the images are cached in Picasso,
so it's working offline provided 
you downloaded the data at least once.

Created by chevil ( Yves Degoyon )
