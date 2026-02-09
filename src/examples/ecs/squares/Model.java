package examples.ecs.squares;

import entity_component_system.EntityComponentSystem;
import entity_component_system.query.Queries;
import processing.core.PApplet;

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

        entityComponentSystem.registerSystem(Message.Interval.class, (msg, _, query) ->
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
                }), Queries.query(Position.class, Velocity.class));

        entityComponentSystem.registerSystem(Message.Draw.class, (msg, commands, query) ->
        {
            final var drawContext = msg.drawContext();

            System.out.println("Now!");

            query.forEach(row ->
            {
                final var pos = row.a();
                final var shape = row.b();

                switch (shape.shape)
                {
                    case Shape.ShapeEnum.Square:
                        System.out.printf("Drawing square at %f, %f with length %f\n", pos.x, pos.y, shape.size);
                        drawContext.square(pos.x, pos.y, shape.size);
                        break;
                    case Shape.ShapeEnum.Circle:
                        System.out.printf("Drawing circle at %f, %f with length %f\n", pos.x, pos.y, shape.size);
                        drawContext.circle(pos.x, pos.y, shape.size);
                        break;
                }
            });
        }, Queries.query(Position.class, Shape.class));

        entityComponentSystem.registerEmptySystem(Message.Spawn.class, (msg, commands) ->
                commands.spawn(msg.position(), msg.velocity(), msg.shape()));

        entityComponentSystem.registerEmptySystem(Message.FlushSpawn.class, (_, commands) ->
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

    public static class Shape
    {
        public float size;

        public ShapeEnum shape;

        public Shape(final float size, final ShapeEnum shape)
        {
            this.size = size;
            this.shape = shape;
        }

        public static Shape square(final float size)
        {
            return new Shape(size, ShapeEnum.Square);
        }

        public static Shape circle(final float size)
        {
            return new Shape(size, ShapeEnum.Circle);
        }

        public enum ShapeEnum
        {
            Square,
            Circle
        }
    }
}
