package com.zsl.bookai.controller;

import com.zsl.bookai.entity.BookEntity;
import com.zsl.bookai.entity.FavoriteEntity;
import com.zsl.bookai.entity.UserEntity;
import com.zsl.bookai.model.ApiResult;
import com.zsl.bookai.repo.BookRepo;
import com.zsl.bookai.repo.FavoriteRepo;
import com.zsl.bookai.repo.UserRepo;
import com.zsl.bookai.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/book")
public class BookController extends BaseController {
    @Autowired
    BookRepo bookRepo;

    @Autowired
    FavoriteRepo favoriteRepo; // 需要注入收藏表的Repository

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/list/tag/{tag}")
    public ApiResult<List<BookEntity>> listBooksByTag(
        @PathVariable("tag") String tag
    ) {
        var books = bookRepo.getAllByTag(tag);
        return success(books);
    }

    @PostMapping("/list")
    public ApiResult<List<BookEntity>> listBooks() {
        var books = bookRepo.getAllBooks();
        return success(books);
    }

    // 新增：批量添加收藏
    @PostMapping("/favorite/add")
    public ApiResult<Void> addFavorites(@RequestBody List<Long> bookIds) {
        String username = getUsername();
        Optional<UserEntity> userOp = userRepo.getByUsername(username);
        if (userOp.isPresent()) {
            UserEntity user = userOp.get();
            try {
                // 先查询已存在的，避免重复插入
                List<Long> existingBookIds = favoriteRepo.findBookIdsByUserId(user.id);

                List<FavoriteEntity> toSave = new ArrayList<>();
                for (Long bookId : bookIds) {
                    if (!existingBookIds.contains(bookId)) {
                        FavoriteEntity favorite = new FavoriteEntity();
                        favorite.userId = user.id;
                        favorite.bookId = bookId;
                        toSave.add(favorite);
                    }
                }

                favoriteRepo.saveAll(toSave);

                return success(null);

            } catch (Exception e) {
                e.printStackTrace();
                return fail("喜欢失败：" + e.getMessage());
            }
        }
        return fail("add favorite fail");
    }

    // BookController.java

    @PostMapping("/favorite/list")
    public ApiResult<List<BookEntity>> getUserFavorites() {
        String username = getUsername();
        Optional<UserEntity> userOp = userRepo.getByUsername(username);
        if (userOp.isEmpty()) {
            return ApiResult.fail("用户不存在");
        }

        UserEntity user = userOp.get();
        var bookIds = favoriteRepo.findBookIdsByUserId(user.id);
        if (bookIds.isEmpty()) {
            return success(Collections.emptyList());
        }

        Iterable<BookEntity> iterable = bookRepo.findAllById(bookIds);
        List<BookEntity> favoriteBooks = StreamSupport
            .stream(iterable.spliterator(), false)
            .collect(Collectors.toList());
        return success(favoriteBooks);
    }

    @PostMapping("/favorite/remove")
    public ApiResult<Void> removeFavorite(
        @RequestBody Long bookId
    ) {
        String username = getUsername();
        Optional<UserEntity> userOp = userRepo.getByUsername(username);

        if (userOp.isPresent()) {
            Long userId = userOp.get().id;
            try {
                favoriteRepo.deleteByUserIdAndBookId(userId, bookId);
                return success(null);
            } catch (Exception e) {
                e.printStackTrace();
                return fail("移除失败：" + e.getMessage());
            }
        }

        return fail("用户信息无效");
    }

}
