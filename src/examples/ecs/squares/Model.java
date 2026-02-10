package examples.ecs.squares;

import entity_component_system.EntityComponentSystem;
import entity_component_system.query.Queries;

public record Model(EntityComponentSystem ecs)
{
    public static Model init()
    {
        final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();

        final Velocity velocity = new Velocity();

        velocity.x = 3.4f * 3;
        velocity.y = -1.6f * 3;

        final Position position = new Position();

        position.x = 500;
        position.y = 500;

        final Shape initialShape = Shape.circle(50f);

        entityComponentSystem.spawn(position, velocity, initialShape);

        // Demonstration of using commands.query in place of a system query.
        entityComponentSystem.registerSystem(Message.Interval.class, (msg, commands) ->
        {
            final var query = commands.query(Queries.query(Position.class, Velocity.class));

            query.forEach(row ->
            {
                final var pos = row.a();

                final var vel = row.b();

                if (pos.x + 50 > msg.width() || pos.x < 0)
                {
                    vel.x = -vel.x;
                }

                if (pos.y + 50 > msg.height() || pos.y < 0)
                {
                    vel.y = -vel.y;
                }

                pos.x += vel.x;
                pos.y += vel.y;

                vel.x *= 0.99f;
                vel.y *= 0.99f;

            });
        });

        entityComponentSystem.registerSystem(Message.Draw.class, (msg, _, query) ->
        {
            final var drawContext = msg.drawContext();

            System.out.println("Now!");

            query.forEach(row ->
            {
                final var pos = row.a();
                final var shape = row.b();

                switch (shape)
                {
                    case final Shape.Square square:
                        System.out.printf("Drawing square at %f, %f with length %f\n", pos.x, pos.y, square.sideLength);
                        drawContext.square(pos.x, pos.y, square.sideLength);
                        break;
                    case final Shape.Circle circle:
                        System.out.printf("Drawing circle at %f, %f with length %f\n", pos.x, pos.y, circle.radius);
                        drawContext.circle(pos.x, pos.y, circle.radius);
                        break;
                }
            });
        }, Queries.query(Position.class, Shape.class));

        entityComponentSystem.registerSystem(Message.Spawn.class, (msg, commands) ->
                commands.spawn(msg.position(), msg.velocity(), msg.shape()));

        entityComponentSystem.registerSystem(Message.FlushSpawn.class, (_, commands) ->
        {
           commands.flushSpawn();
        });

        return new Model(entityComponentSystem);
    }

    public static class Position
    {
        public float x;
        public float y;
    }

    public static class Velocity
    {
        public float x;
        public float y;
    }

    public sealed interface Shape permits Shape.Square, Shape.Circle
    {
        static Shape circle(final float radius)
        {
            return new Circle(radius);
        }

        static Shape square(final float sideLength)
        {
            return new Square(sideLength);
        }

        final class Square implements Shape
        {
            public float sideLength;

            public Square(final float sideLength)
            {
                this.sideLength = sideLength;
            }
        }

        final class Circle implements Shape
        {
            public float radius;

            public Circle(final float radius)
            {
                this.radius = radius;
            }
        }
    }

//    public static class Shape
//    {
//        public final float size;
//
//        public final ShapeEnum shape;
//
//        public Shape(final float size, final ShapeEnum shape)
//        {
//            this.size = size;
//            this.shape = shape;
//        }
//
//        public static Shape square(final float size)
//        {
//            return new Shape(size, ShapeEnum.Square);
//        }
//
//        public static Shape circle(final float size)
//        {
//            return new Shape(size, ShapeEnum.Circle);
//        }
//
//        public enum ShapeEnum
//        {
//            Square,
//            Circle
//        }
//    }
}
