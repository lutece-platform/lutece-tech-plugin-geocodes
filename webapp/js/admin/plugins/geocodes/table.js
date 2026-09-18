/*
 * Sorts a table on one of its columns, alternating ascending and descending on each call.
 *
 * The screens used to load this from the identitystore plugin, which a site deploying geocodes alone does not
 * have: every screen answered a 404 for it and the column headers did nothing.
 */
function sortTable( n, tableId )
{
    var table = document.getElementById( tableId );
    if ( !table )
    {
        return;
    }

    var switching = true;
    var dir = 'asc';
    var switchcount = 0;

    while ( switching )
    {
        switching = false;
        var rows = table.rows;
        var i;

        for ( i = 1; i < ( rows.length - 1 ); i++ )
        {
            var x = rows[i].getElementsByTagName( 'TD' )[n];
            var y = rows[i + 1].getElementsByTagName( 'TD' )[n];

            if ( !x || !y )
            {
                continue;
            }

            var shouldSwitch = dir === 'asc'
                ? x.innerText.toLowerCase( ) > y.innerText.toLowerCase( )
                : x.innerText.toLowerCase( ) < y.innerText.toLowerCase( );

            if ( shouldSwitch )
            {
                rows[i].parentNode.insertBefore( rows[i + 1], rows[i] );
                switching = true;
                switchcount++;
                break;
            }
        }

        if ( !switching && switchcount === 0 && dir === 'asc' )
        {
            dir = 'desc';
            switching = true;
        }
    }
}
