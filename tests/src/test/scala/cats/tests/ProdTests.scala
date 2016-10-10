package cats
package tests

import cats.data.Prod
import cats.functor.Contravariant
import cats.laws.discipline._
import cats.laws.discipline.arbitrary._
import cats.laws.discipline.eq._

class ProdTests extends CatsSuite {
  implicit val iso = CartesianTests.Isomorphisms.invariant[Prod[Option, List, ?]]
  checkAll("Prod[Option, List, Int]", CartesianTests[λ[α => Prod[Option, List, α]]].cartesian[Int, Int, Int])
  checkAll("Cartesian[Prod[Option, List, Int]]", SerializableTests.serializable(Cartesian[λ[α => Prod[Option, List, α]]]))

  checkAll("Prod[Option, List, Int]", AlternativeTests[λ[α => Prod[Option, List, α]]].alternative[Int, Int, Int])
  checkAll("Alternative[Prod[Option, List, Int]]", SerializableTests.serializable(Alternative[λ[α => Prod[Option, List, α]]]))

  checkAll("Prod[Show, Order, Int]", ContravariantTests[λ[α => Prod[Show, Order, α]]].contravariant[Int, Int, Int])
  checkAll("Contravariant[Prod[Show, Order, Int]]", SerializableTests.serializable(Contravariant[λ[α => Prod[Show, Order, α]]]))

  checkAll("Show[Prod[Option, Option, Int]]", SerializableTests.serializable(Show[Prod[Option, Option, Int]]))

  {
    implicit val monoidK = ListWrapper.monoidK
    checkAll("Prod[ListWrapper, ListWrapper, ?]", MonoidKTests[Prod[ListWrapper, ListWrapper, ?]].monoidK[Int])
    checkAll("MonoidK[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(MonoidK[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val semigroupK = ListWrapper.semigroupK
    checkAll("Prod[ListWrapper, ListWrapper, ?]", SemigroupKTests[Prod[ListWrapper, ListWrapper, ?]].semigroupK[Int])
    checkAll("SemigroupK[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(SemigroupK[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val apply = ListWrapper.applyInstance
    implicit val iso = CartesianTests.Isomorphisms.invariant[Prod[ListWrapper, ListWrapper, ?]]
    checkAll("Prod[ListWrapper, ListWrapper, ?]", ApplyTests[Prod[ListWrapper, ListWrapper, ?]].apply[Int, Int, Int])
    checkAll("Apply[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(Apply[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val functor = ListWrapper.functor
    checkAll("Prod[ListWrapper, ListWrapper, ?]", FunctorTests[Prod[ListWrapper, ListWrapper, ?]].functor[Int, Int, Int])
    checkAll("Functor[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(Functor[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val monad = ListWrapper.monad
    implicit val iso = CartesianTests.Isomorphisms.invariant[Prod[ListWrapper, ListWrapper, ?]]
    checkAll("Prod[ListWrapper, ListWrapper, ?]", MonadTests[Prod[ListWrapper, ListWrapper, ?]].monad[Int, Int, Int])
    checkAll("Monad[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(Monad[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val foldable = ListWrapper.foldable
    checkAll("Prod[ListWrapper, ListWrapper, ?]", FoldableTests[Prod[ListWrapper, ListWrapper, ?]].foldable[Int, Int])
    checkAll("Foldable[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(Foldable[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val traverse = ListWrapper.traverse
    checkAll("Prod[ListWrapper, ListWrapper, ?]", TraverseTests[Prod[ListWrapper, ListWrapper, ?]].traverse[Int, Int, Int, Int, Option, Option])
    checkAll("Traverse[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(Traverse[Prod[ListWrapper, ListWrapper, ?]]))
  }

  {
    implicit val monadCombine = ListWrapper.monadCombine
    implicit val iso = CartesianTests.Isomorphisms.invariant[Prod[ListWrapper, ListWrapper, ?]]
    checkAll("Prod[ListWrapper, ListWrapper, ?]", MonadCombineTests[Prod[ListWrapper, ListWrapper, ?]].monadCombine[Int, Int, Int])
    checkAll("MonadCombine[Prod[ListWrapper, ListWrapper, ?]]", SerializableTests.serializable(MonadCombine[Prod[ListWrapper, ListWrapper, ?]]))
  }

  test("order") {
    forAll { t: Prod[Id, Id, Int] =>
      val u: Prod[Id, Id, Int] = Prod(t.second, t.first)
      val Prod(t1, t2) = t
      val Prod(u1, u2) = u

      Order[Prod[Id, Id, Int]].compare(t, u) should === (Order[(Int, Int)].compare((t1, t2), (u1, u2)))
    }
  }

  test("show") {
    forAll { (l1: Option[Int], l2: Option[Int]) =>
      val showOptionInt = implicitly[Show[Option[Int]]]
      val prod = Prod(l1, l2)

      Show[Prod[Option, Option, Int]].show(prod) should === (s"Prod(${showOptionInt.show(l1)}, ${showOptionInt.show(l2)})")
    }
  }

}
