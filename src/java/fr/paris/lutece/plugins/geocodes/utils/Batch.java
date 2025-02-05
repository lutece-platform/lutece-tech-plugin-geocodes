package fr.paris.lutece.plugins.geocodes.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public final class Batch<T> extends AbstractList<List<T>>
{
    private final List<T> innerList;
    private final int partitionSize;

    public Batch( final List<T> innerList, final int partitionSize )
    {
        this.innerList = new ArrayList<>(innerList );
        this.partitionSize = partitionSize;
    }

    public static <T> Batch<T> ofSize( final List<T> list, final int partitionSize )
    {
        return new Batch<>( list, partitionSize );
    }

    @Override
    public List<T> get( final int index )
    {
        int start = index * this.partitionSize;
        int end = Math.min( start + this.partitionSize, this.innerList.size( ) );

        if ( start > end )
        {
            throw new IndexOutOfBoundsException( "Index " + index + " is out of the list range <0," + ( size( ) - 1 ) + ">" );
        }

        return new ArrayList<>( this.innerList.subList( start, end ) );
    }

    @Override
    public int size( )
    {
        return partitionSize == 0 ? 0 : (int) Math.ceil( (double) this.innerList.size( ) / (double) this.partitionSize );
    }

    public int totalSize( )
    {
        return this.innerList.size( );
    }
}
