package examples.ecs.ai.behaviour;

import entity_component_system.components.space.Position;
import entity_component_system.components.space.Velocity;
import entity_component_system.query.Commands;
import entity_component_system.query.Queries;
import examples.ecs.ai.EnemyState;
import examples.ecs.ai.Route;
import examples.ecs.ai.messages.Tick;
import examples.ecs.movement.components.Player;
import examples.ecs.movement.physics.collision.Collider2D;
import processing.core.PVector;

import static examples.ecs.ai.AIExampleModel.*;
import static examples.ecs.ai.Geographic.locationToPVector;
import static examples.ecs.ai.Geographic.vectorsClose;

public class DirectEnemies
{
    public static void directEnemy(final Tick tick, final Commands commands)
    {
        final var enemiesQuery = commands.query(Queries.query(Collider2D.class, Position.class, Velocity.class, EnemyState.class, Route.class).without(Player.class));

        for (final var row : enemiesQuery)
        {
            final var peeked = row.e().route.peek();

            peeked.ifPresent(nextLocation ->
            {
                if (vectorsClose(locationToPVector(nextLocation), row.b()))
                {
                    row.e().route.dequeue();
                    final var nextLocationMaybe = row.e().route.peek();
                    if (nextLocationMaybe.isEmpty()) return;
                    nextLocation = nextLocationMaybe.get();
                }

                final PVector toTarget = locationToPVector(nextLocation).copy().sub(row.b());
                final float distance = toTarget.mag();

                if (distance > 0)
                {
                    row.c().set(toTarget.normalize().mult(Math.min(AIMaximumSpeedFactor, distance)));
                }
            });
        }
    }
}
