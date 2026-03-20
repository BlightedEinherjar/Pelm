package examples.ecs.movement.pattern;

import entity_component_system.components.space.Position;
import entity_component_system.query.Query4;
import examples.ecs.movement.components.IsFree;
import examples.ecs.movement.drawing.Rectangle;
import examples.ecs.movement.physics.collision.Collider2D;
import utils.row.Row4;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

import static examples.ecs.movement.Model.SquareSize;
import static examples.ecs.movement.Movement.RenderSize;

public enum Patterns
{
    Steps(Patterns::steps),
    Swing(Patterns::swing)
    ;

    private static void swing(final int layerCount, final Query4<Collider2D, examples.ecs.movement.drawing.Rectangle, Position, IsFree> query)
    {
        final int decreaseAmount = (layerCount + 1) * 5 * SquareSize;

        final var iterator = query.iterator();
        final var stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

        final List<Row4<Collider2D, examples.ecs.movement.drawing.Rectangle, Position, IsFree>> list =
                stream.filter(x -> x.d().free).limit(9).toList();

        if (list.size() != 9) return;

        list.forEach(x -> x.d().free = false);

        final boolean side = (layerCount + 1) % 2 == 0;

        final Color levelColour = side ? Color.black : Color.white;
        final Color blockColour = side ? Color.red : Color.blue;


        final var first = list.getFirst();
        first.a().width = SquareSize;
        first.a().height = 1;
        first.b().width = SquareSize;
        first.b().height = 1;

        final int firstX = 0;
        first.c().x = side ? firstX : mirrorX(firstX, first.a().width);
        first.c().y = -decreaseAmount + 5 * SquareSize;

        first.b().color = levelColour;

        final var second = list.get(1);
        second.a().width = SquareSize * 6;
        second.a().height = 1;
        second.b().width = SquareSize * 6;
        second.b().height = 1;

        final int secondX = SquareSize * 2;
        second.c().x = side ? secondX : mirrorX(secondX, second.a().width);
        second.c().y = -decreaseAmount + 5 * SquareSize;

        second.b().color = levelColour;

        final var third = list.get(2);
        third.a().width = SquareSize;
        third.a().height = 1;
        third.b().width = SquareSize;
        third.b().height = 1;

        final int thirdX = SquareSize * 9;
        third.c().x = side ? thirdX : mirrorX(thirdX, third.a().width);
        third.c().y = -decreaseAmount + 5 * SquareSize;

        third.b().color = levelColour;


        final var fourth = list.get(3);
        fourth.a().width = SquareSize;
        fourth.a().height = 2 * SquareSize;
        fourth.b().width = SquareSize;
        fourth.b().height = 2 * SquareSize;

        final int fourthX = 0;
        fourth.c().x = side ? fourthX : mirrorX(fourthX, fourth.a().width);
        fourth.c().y = -decreaseAmount + 3 * SquareSize;

        fourth.b().color = blockColour;


        final var fifth = list.get(4);
        fifth.a().width = SquareSize;
        fifth.a().height = 2 * SquareSize;
        fifth.b().width = SquareSize;
        fifth.b().height = 2 * SquareSize;

        final int fifthX = 2 * SquareSize;
        fifth.c().x = side ? fifthX : mirrorX(fifthX, fifth.a().width);
        fifth.c().y = -decreaseAmount + 3 * SquareSize;

        fifth.b().color = blockColour;


        final var sixth = list.get(5);
        sixth.a().width = SquareSize;
        sixth.a().height = SquareSize;

        sixth.b().width = SquareSize;
        sixth.b().height = SquareSize;

        final int sixthX = 4 * SquareSize;
        sixth.c().x = side ? sixthX : mirrorX(sixthX, sixth.a().width);
        sixth.c().y = -decreaseAmount + 3 * SquareSize;

        sixth.b().color = blockColour;

        final var seventh = list.get(6);
        seventh.a().width = 2 * SquareSize;
        seventh.a().height = SquareSize;
        seventh.b().width = 2 * SquareSize;
        seventh.b().height = SquareSize;

        final int seventhX = 7 * SquareSize;

        seventh.c().x = side ? seventhX : mirrorX(seventhX, seventh.a().width);
        seventh.c().y = -decreaseAmount + 3 * SquareSize;

        seventh.b().color = blockColour;

        final var eighth = list.get(7);

        eighth.a().width = SquareSize;
        eighth.a().height = SquareSize;
        eighth.b().width = SquareSize;
        eighth.b().height = SquareSize;
        final int eighthX = 8 * SquareSize;
        eighth.c().x = side ? eighthX : mirrorX(eighthX, eighth.a().width);
        eighth.c().y = -decreaseAmount + 1 * SquareSize;

        eighth.b().color = blockColour;

        final var ninth = list.get(8);

        ninth.a().width = SquareSize;
        ninth.a().height = SquareSize;
        ninth.b().width = SquareSize;
        ninth.b().height = SquareSize;
        final int ninthX = 6 * SquareSize;
        ninth.c().x = side ? ninthX : mirrorX(ninthX, ninth.a().width);
        ninth.c().y = -decreaseAmount + 1 * SquareSize;

        ninth.b().color = blockColour;
    }

    private static void steps(final int layerCount, final Query4<Collider2D, examples.ecs.movement.drawing.Rectangle, Position, IsFree> query)
    {
        final int decreaseAmount = (layerCount + 1) * 5 * SquareSize;

        final var iterator = query.iterator();
        final var stream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);

        final List<Row4<Collider2D, examples.ecs.movement.drawing.Rectangle, Position, IsFree>> list =
                stream.filter(x -> x.d().free).limit(8).toList();

        if (list.size() != 8) return;

        list.forEach(x -> x.d().free = false);

        final boolean side = (layerCount + 1) % 2 == 0;

        final Color levelColour = side ? Color.black : Color.white;
        final Color blockColour = side ? Color.red : Color.blue;

        final var first = list.getFirst();
        first.a().width = SquareSize;
        first.a().height = 1;
        first.b().width = SquareSize;
        first.b().height = 1;

        final int firstX = 0;
        first.c().x = side ? firstX : mirrorX(firstX, first.a().width);
        first.c().y = -decreaseAmount + 5 * SquareSize;

        first.b().color = levelColour;

        final var second = list.get(1);
        second.a().width = SquareSize * 6;
        second.a().height = 1;
        second.b().width = SquareSize * 6;
        second.b().height = 1;

        final int secondX = SquareSize * 2;
        second.c().x = side ? secondX : mirrorX(secondX, second.a().width);
        second.c().y = -decreaseAmount + 5 * SquareSize;

        second.b().color = levelColour;

        final var third = list.get(2);
        third.a().width = SquareSize;
        third.a().height = 1;
        third.b().width = SquareSize;
        third.b().height = 1;

        final int thirdX = SquareSize * 9;
        third.c().x = side ? thirdX : mirrorX(thirdX, third.a().width);
        third.c().y = -decreaseAmount + 5 * SquareSize;

        third.b().color = levelColour;

        final var fourth = list.get(3);
        fourth.a().width = SquareSize;
        fourth.a().height = SquareSize;
        fourth.b().width = SquareSize;
        fourth.b().height = SquareSize;

        final int fourthX = SquareSize * 3;
        fourth.c().x = side ? fourthX : mirrorX(fourthX, fourth.a().width);
        fourth.c().y = -decreaseAmount + 4 * SquareSize;

        fourth.b().color = blockColour;

        final var fifth = list.get(4);
        fifth.a().width = SquareSize;
        fifth.a().height = SquareSize * 2;

        fifth.b().width = SquareSize;
        fifth.b().height = SquareSize * 2;

        final int fifthX = SquareSize * 4;
        fifth.c().x = side ? fifthX : mirrorX(fifthX, fifth.a().width);
        fifth.c().y = -decreaseAmount + 3 * SquareSize;

        fifth.b().color = blockColour;

        final var sixth = list.get(5);
        sixth.a().width = SquareSize * 2;
        sixth.a().height = SquareSize * 3;

        sixth.b().width = SquareSize * 2;
        sixth.b().height = SquareSize * 3;

        final int sixthX = SquareSize * 5;
        sixth.c().x = side ? sixthX : mirrorX(sixthX, sixth.a().width);
        sixth.c().y = -decreaseAmount + 2 * SquareSize;

        sixth.b().color = blockColour;

        final var seventh = list.get(6);

        seventh.a().width = SquareSize;
        seventh.a().height = SquareSize * 4;

        seventh.b().width = SquareSize;
        seventh.b().height = SquareSize * 4;

        final int seventhX = SquareSize * 7;
        seventh.c().x = side ? seventhX : mirrorX(seventhX, seventh.a().width);
        seventh.c().y = -decreaseAmount + SquareSize;

        seventh.b().color = blockColour;


        final var eighth = list.get(7);

        eighth.a().width = SquareSize;
        eighth.a().height = SquareSize * 5;

        eighth.b().width = SquareSize;
        eighth.b().height = SquareSize * 5;

        final int eighthX = SquareSize * 9;
        eighth.c().x = side ? eighthX : mirrorX(eighthX, eighth.a().width);
        eighth.c().y = -decreaseAmount;

        eighth.b().color = blockColour;
    }

    static int mirrorX(final int x, final int width) {
        return RenderSize.a() - x - width;
    }

    public final Pattern pattern;

    Patterns(final BiConsumer<Integer, Query4<Collider2D, Rectangle, Position, IsFree>> patternFunction)
    {
        this.pattern = new Pattern(patternFunction);
    }
}
