package examples.ecs.squares;

import entity_component_system.EntityComponentSystem;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import entity_component_system.query.Query1;
import entity_component_system.query.Query2;

public record Model(EntityComponentSystem ecs)
{
    public static Model init()
    {
        final EntityComponentSystem entityComponentSystem = new EntityComponentSystem();

        // Demonstration of using commands.query in place of a system query.
        entityComponentSystem
                .registerSystem(Message.Interval.class, Model::moveParticles)
                .registerSystem(Message.Interval.class, Model::collideParticles)
                .registerSystem(Message.Draw.class, Model::drawParticles, Queries.query(Position.class, Shape.class))
                .registerSystem(Message.Spawn.class, Model::spawn)
                .registerSystem(Message.FlushSpawn.class, Model::flushSpawn);

        return new Model(entityComponentSystem);
    }

    private static void collideParticles(final Message.Interval interval, final Commands commands)
    {
        final var query = commands.query(Queries.query(Position.class, Shape.class));

        query.forEach(row1 ->
        {
            query.forEach(row2 ->
            {

            });
        });
    }

    private static void moveParticles(final Message.Interval msg, final Commands commands)
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
    }

    private static void drawParticles(final Message.Draw msg, final Commands commands, final Query2<Position, Shape> query)
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
    }

    private static void spawn(final Message.Spawn msg, final Commands commands)
    {
        commands.spawn(msg.position(), msg.velocity(), msg.shape());
    }

    private static void flushSpawn(final Message.FlushSpawn msg, final Commands commands)
    {
        commands.flushSpawn();
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
}
