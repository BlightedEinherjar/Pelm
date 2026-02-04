package examples.pong;

import core.Pelm;
import core.Subscription;
import examples.pong.message.Message;
import examples.pong.model.*;
import examples.pong.utils.Vec2;
import processing.core.PFont;

import examples.pong.message.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static examples.pong.model.Ball.*;
import static examples.pong.model.Paddle.PaddleHeight;
import static examples.pong.model.Paddle.PaddleWidth;

public class Pong extends Pelm<Model, Message>
{
    private static Paddle initPaddle()
    {
        return new Paddle(0.5f, EnumSet.noneOf(Direction.class), 0);
    }

    private static Ball initBall()
    {
        return new Ball(new Vec2(0.5f, 0.5f), new Vec2(InitialBallSpeed, 0f), InitialBallSpeed);
    }

    public Pong()
    {
        this(new Model(initPaddle(), initPaddle(), initBall()));
    }

    private Pong(final Model model)
    {
        super(model);
    }

    private final List<Subscription<Message>> subs = Subscriptions.subscriptions(millis());

    @Override
    protected Stream<? extends Subscription<Message>> subscriptions(final Model model)
    {
        return subs.stream();
    }

    @Override
    public void settings() {
        fullScreen();
    }

    @Override
    protected void onSetup()
    {
        final PFont font = createFont("PongFont/pong-score.ttf", 128);

        textFont(font);

        textSize(100f);

        // Looks funky with this
        strokeWeight(10f);
    }

    @Override
    protected void view(final Model model)
    {
        background(120);

        // Centre line
        line(0f, 0.5f * height, width, 0.5f * height);

        final Vec2 leftPlayerCorner  = model.leftPlayer().topLeftCorner(Player.Left).toScreenSpace(width, height);
        final Vec2 rightPlayerCorner = model.rightPlayer().topLeftCorner(Player.Right).toScreenSpace(width, height);

        // Draw left paddle
        rect(leftPlayerCorner.x(),
                leftPlayerCorner.y(),
                PaddleWidth * width,
                PaddleHeight * height);

        // Draw right paddle
        rect(rightPlayerCorner.x(),
                rightPlayerCorner.y(),
                PaddleWidth * width,
                PaddleHeight * height);

        final var ballCorner = model.ball().topLeftCorner().toScreenSpace(width, height);

        // Draw ball
        rect(ballCorner.x(),
                ballCorner.y(),
                BallWidth * width,
                BallHeight * height);

        // Score!
        text(model.leftPlayer().score(), 0.4f * displayWidth, 0.2f * displayHeight);
        text(model.rightPlayer().score(), 0.6f * displayWidth, 0.2f * displayHeight);
    }

    @Override
    protected Model update(final Message message, final Model model)
    {
        return switch (message)
        {
            case final None _ -> model;
            case final AddDirection b -> switch (b.player())
            {
                case Left -> model.withLeftDirection(b.direction());
                case Right -> model.withRightDirection(b.direction());
            };
            case final RemoveDirection b -> switch (b.player())
            {
                case Left -> model.withoutLeftDirection(b.direction());
                case Right -> model.withoutRightDirection(b.direction());
            };
            case final Interval _ -> model
                    .handlePaddleCollisions()
                    .handleWallCollisions()
                    .handleScoring()
                    .movePaddles()
                    .moveBall();
        };
    }
}
